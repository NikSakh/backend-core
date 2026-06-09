package ru.mentee.power.crm.jparepository;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mentee.power.crm.domain.jpa.LeadJpaEntity;

@Repository
public interface LeadJpaRepository extends JpaRepository<LeadJpaEntity, UUID> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT l FROM LeadJpaEntity l WHERE l.id = :id")
  Optional<LeadJpaEntity> findByIdForUpdate(@Param("id") UUID id);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT l FROM LeadJpaEntity l WHERE l.email = :email")
  Optional<LeadJpaEntity> findByEmailForUpdate(@Param("email") String email);
}