package ru.mentee.power.crm.jparepository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.mentee.power.crm.domain.jpa.LeadJpaEntity;

@Repository
public interface LeadJpaRepository extends JpaRepository<LeadJpaEntity, UUID> {

  @Query(value = "SELECT * FROM leads WHERE email = ?1", nativeQuery = true)
  Optional<LeadJpaEntity> findByEmailNative(String email);
}