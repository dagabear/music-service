package kz.genvibe.media_management.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MusicAtmosphere {
    WARM_AND_WELCOMING("Warm & Welcoming"),
    MODERN_AND_ENERGETIC("Modern & Energetic"),
    PREMIUM_AND_SOPHISTICATED("Premium & Sophisticated"),
    SOCIAL_AND_STYLISH("Social & Stylish"),
    CREATIVE_AND_PLAYFUL("Creative & Playful"),
    NATURAL_AND_MINDFUL("Natural & Mindful"),
    TRADITIONAL_AND_CULTURAL_CHINESE("Traditional & Cultural (Chinese)"),
    TRADITIONAL_AND_CULTURAL_JAPANESE("Traditional & Cultural (Japanese)");

    private final String name;
}
