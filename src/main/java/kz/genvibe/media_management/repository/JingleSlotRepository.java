package kz.genvibe.media_management.repository;

import kz.genvibe.media_management.model.entity.JingleSlot;
import kz.genvibe.media_management.model.enums.JingleSlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface JingleSlotRepository extends JpaRepository<JingleSlot, Long> {
    List<JingleSlot> findJingleSlotsByPlayTimeAndStatusAndJingleRequestedToPauseIsFalse(
        LocalDateTime playTime,
        JingleSlotStatus status
    );
}
