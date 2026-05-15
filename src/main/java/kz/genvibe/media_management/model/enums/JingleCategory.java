package kz.genvibe.media_management.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JingleCategory {
    PROMOTIONAL("Promotional"),
    OPERATIONAL("Operational"),
    BRANDING("Branding"),;

    private final String name;
}
