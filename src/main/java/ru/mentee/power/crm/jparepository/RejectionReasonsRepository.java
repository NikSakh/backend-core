package ru.mentee.power.crm.jparepository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mentee.power.crm.domain.jpa.RejectionReasons;

@Repository
public interface RejectionReasonsRepository extends JpaRepository<RejectionReasons, UUID> {

  List<RejectionReasons> findByActiveTrue();
}
