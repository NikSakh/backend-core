package ru.mentee.power.crm.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.domain.jpa.LeadJpaEntity;
import ru.mentee.power.crm.jparepository.LeadJpaRepository;

@SpringBootTest(classes = ru.mentee.power.crm.spring.Application.class)
@ActiveProfiles("test")
class LeadLockingServiceTest {

  @Autowired private LeadLockingService service;

  @Autowired private LeadJpaRepository repository;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  void shouldSerializeWithPessimisticLock() throws Exception {
    LeadJpaEntity lead = repository.save(new LeadJpaEntity("pessimistic@test.com", "Corp", "NEW"));

    ExecutorService executor = Executors.newFixedThreadPool(2);
    CountDownLatch latch = new CountDownLatch(1);

    executor.submit(
        () -> {
          try {
            latch.await();
            service.updateWithPessimisticLock(lead.getId(), "CONTACTED");
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        });

    executor.submit(
        () -> {
          try {
            latch.await();
            service.updateWithPessimisticLock(lead.getId(), "QUALIFIED");
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        });

    latch.countDown();
    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);

    LeadJpaEntity found = repository.findById(lead.getId()).get();
    assertThat(found.getStatus()).isIn("CONTACTED", "QUALIFIED");
  }

  @Test
  void shouldThrowOptimisticLockExceptionOnConflict() throws Exception {
    LeadJpaEntity lead = repository.save(new LeadJpaEntity("optimistic@test.com", "Corp", "NEW"));

    ExecutorService executor = Executors.newFixedThreadPool(2);
    CountDownLatch latch = new CountDownLatch(1);

    executor.submit(
        () -> {
          try {
            latch.await();
            service.updateWithOptimisticLock(lead.getId(), "CONTACTED");
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        });

    executor.submit(
        () -> {
          try {
            latch.await();
            Thread.sleep(100);
            service.updateWithOptimisticLock(lead.getId(), "QUALIFIED");
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        });

    latch.countDown();
    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);

    LeadJpaEntity found = repository.findById(lead.getId()).get();
    assertThat(found.getStatus()).isIn("CONTACTED", "QUALIFIED");
  }
}

