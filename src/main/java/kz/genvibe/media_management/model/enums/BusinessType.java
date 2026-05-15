package kz.genvibe.media_management.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BusinessType {
    CASUAL_DINING("Casual Dining"),
    CAFE_BAKERY("Cafe / Bakery"),
    BAR_NIGHTLIFE("Bar / Nightlife"),
    FAST_FOOD("Fast Food"),
    LUXURY_BOUTIQUE("Luxury Boutique"),
    APPAREL_FASHION("Apparel / Fashion"),
    SUPERMARKET("Supermarket"),
    BOUTIQUE_HOTEL("Boutique Hotel"),
    RESORT("Resort"),
    SPA_WELLNESS("Spa / Wellness"),
    HOTEL_LOBBY("Hotel Lobby"),
    OTHER("Other");

    private final String name;
}
