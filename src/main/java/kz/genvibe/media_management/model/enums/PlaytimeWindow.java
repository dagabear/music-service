package kz.genvibe.media_management.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PlaytimeWindow {
    MORNING("Morning only"),
    LUNCH("Lunch/peak hours only"),
    EVENING("Evening only"),
    ALL_DAY("All day");

    private final String name;
}
