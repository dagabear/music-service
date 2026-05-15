package kz.genvibe.media_management.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MusicMood {
    SLOW_DOWN_AND_RELAX(
        "Slow down and relax",
        MusicMood.DESCRIPTION_PREFIX + "cafés, wellness spaces, spas, beach clubs (daytime), casual " +
            "dining, boutique hotels."
    ),
    FEEL_COMFORTABLE_STAYING_LONGER(
        "Feel comfortable staying longer",
        MusicMood.DESCRIPTION_PREFIX + "casual restaurants, wine bars, lifestyle retail, dessert cafés, social " +
            "dining concepts."
    ),
    FEEL_ENERGIZED_AND_SOCIAL(
        "Feel energized and social",
        MusicMood.DESCRIPTION_PREFIX + "trendy restaurants, bars, rooftop venues, youth retail brands, " +
            "sportswear stores, fast-casual dining."
    ),
    FEEL_LUXURIOUS_AND_SOPHISTICATED(
        "Feel luxurious and sophisticated",
        MusicMood.DESCRIPTION_PREFIX + "luxury retail, fine dining, hotel lounges, premium salons, flagship " +
            "stores."
    ),
    MOVE_EFFICIENTLY_AND_DECIDE_QUICKLY(
        "Move efficiently and decide quickly",
        MusicMood.DESCRIPTION_PREFIX + "fast food chains, convenience retail, fast fashion, grab-and-go " +
            "cafés, high-footfall mall stores."
    );

    private final String name;
    private final String description;

    private static final String DESCRIPTION_PREFIX = "Best suited for: ";
}
