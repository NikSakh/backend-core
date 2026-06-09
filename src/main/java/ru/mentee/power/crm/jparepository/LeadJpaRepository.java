package ru.mentee.power.crm.jparepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mentee.power.crm.domain.jpa.LeadJpaEntity;

@Repository
public interface LeadJpaRepository extends JpaRepository<LeadJpaEntity, UUID> {

  @Query(value = "SELECT * FROM leads WHERE email = ?1", nativeQuery = true)
  Optional<LeadJpaEntity> findByEmailNative(String email);

  @Query(value = "SELECT * FROM leads WHERE status = ?1", nativeQuery = true)
  List<LeadJpaEntity> findByStatusNative(String status);

  Optional<LeadJpaEntity> findByEmail(String email);

  List<LeadJpaEntity> findByStatus(String status);

  List<LeadJpaEntity> findByCompany(String company);

  long countByStatus(String status);

  boolean existsByEmail(String email);

  List<LeadJpaEntity> findByEmailContaining(String emailPart);

  List<LeadJpaEntity> findByStatusAndCompany(String status, String company);

  @Query("SELECT l FROM LeadJpaEntity l WHERE l.status IN :statuses")
  List<LeadJpaEntity> findByStatusIn(@Param("statuses") List<String> statuses);

  @Query("SELECT l FROM LeadJpaEntity l WHERE l.company = :company ORDER BY l.status ASC")
  List<LeadJpaEntity> findByCompanyOrderedByStatus(@Param("company") String company);

  Page<LeadJpaEntity> findAll(Pageable pageable);

  Page<LeadJpaEntity> findByStatus(String status, Pageable pageable);

  Page<LeadJpaEntity> findByCompany(String company, Pageable pageable);

  @Query("SELECT l FROM LeadJpaEntity l WHERE l.status IN :statuses")
  Page<LeadJpaEntity> findByStatusInPaged(@Param("statuses")
                                          List<String> statuses, Pageable pageable);

  @Modifying(clearAutomatically = true)
  @Query("UPDATE LeadJpaEntity l SET l.status = :newStatus WHERE l.status = :oldStatus")
  int updateStatusBulk(@Param("oldStatus") String oldStatus, @Param("newStatus") String newStatus);
}