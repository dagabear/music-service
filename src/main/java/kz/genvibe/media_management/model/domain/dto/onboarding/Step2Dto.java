package kz.genvibe.media_management.model.domain.dto.onboarding;

import jakarta.validation.constraints.NotNull;
import kz.genvibe.media_management.model.enums.MusicAtmosphere;

public record Step2Dto(
    @NotNull
    MusicAtmosphere atmosphere
) { }
