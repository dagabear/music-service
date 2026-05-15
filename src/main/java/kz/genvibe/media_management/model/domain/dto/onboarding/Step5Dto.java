package kz.genvibe.media_management.model.domain.dto.onboarding;

import jakarta.validation.constraints.NotNull;
import kz.genvibe.media_management.model.enums.PlaytimeWindow;

public record Step5Dto(
    @NotNull PlaytimeWindow playtimeWindow
) { }
