package kz.genvibe.media_management.model.domain.dto.onboarding;

import jakarta.validation.constraints.NotEmpty;
import kz.genvibe.media_management.model.enums.CurrentFeel;

import java.util.List;

public record Step3Dto(
    @NotEmpty List<CurrentFeel> currentFeels
) { }
