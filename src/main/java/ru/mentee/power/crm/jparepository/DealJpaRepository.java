package ru.mentee.power.crm.jparepository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mentee.power.crm.domain.jpa.DealJpaEntity;

@Repository
public interface DealJpaRepository extends JpaRepository<DealJpaEntity, UUID> {

  @EntityGraph(attributePaths = {"dealProducts", "dealProducts.product"})
  @Query("SELECT d FROM DealJpaEntity d WHERE d.id = :id")
  Optional<DealJpaEntity> findDealWithProducts(@Param("id") UUID id);
}
