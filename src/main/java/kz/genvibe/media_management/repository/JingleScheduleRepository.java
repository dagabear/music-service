package kz.genvibe.media_management.repository;

import kz.genvibe.media_management.model.entity.JingleSchedule;
import kz.genvibe.media_management.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JingleScheduleRepository extends JpaRepository<JingleSchedule, Long> {
    Optional<JingleSchedule> findJingleScheduleByStore(Store store);
}
