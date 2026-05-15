package kz.genvibe.media_management.model.domain.dto.onboarding;

import jakarta.validation.constraints.NotNull;
import kz.genvibe.media_management.model.enums.MusicMood;

public record Step4Dto(
    @NotNull MusicMood mood
) { }
