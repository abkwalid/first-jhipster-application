package tech.digiwise.japp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.digiwise.japp.domain.DailyTaskLog;

/**
 * Spring Data JPA repository for the DailyTaskLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DailyTaskLogRepository extends JpaRepository<DailyTaskLog, Long> {}
