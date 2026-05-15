package kz.genvibe.media_management.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Getter
@RequiredArgsConstructor
public enum JingleRepeatingTime {
    EVERY_HOUR(Duration.ofHours(1), "Every hour"),
    EVERY_TWO_HOURS(Duration.ofHours(2), "Every 2 hours"),
    EVERY_THREE_HOURS(Duration.ofHours(3), "Every 3 hours"),
    EVERY_DAY(Duration.ofDays(1), "Every day"),
    EVERY_WEEK(Duration.ofDays(7), "Every week");

    private final Duration duration;
    private final String name;
}
