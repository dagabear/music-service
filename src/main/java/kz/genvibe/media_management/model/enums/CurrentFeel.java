package kz.genvibe.media_management.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CurrentFeel {
    BUSY_AND_FAST_MOVING("Busy & Fast-moving"),
    CALM_AND_QUIET("Calm & Quiet"),
    SOCIAL_AND_LIVELY("Social & Lively"),
    FOCUSED_AND_TASK_ORIENTED("Focused & Task-oriented"),
    POLISHED_AND_PREMIUM("Polished & Premium"),
    CASUAL_AND_FAMILIAR("Casual & Family");

    private final String name;
}
