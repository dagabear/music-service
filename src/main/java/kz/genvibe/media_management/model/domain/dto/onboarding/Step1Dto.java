package kz.genvibe.media_management.model.domain.dto.onboarding;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kz.genvibe.media_management.model.enums.BusinessType;
import kz.genvibe.media_management.model.enums.MusicProvider;

public record Step1Dto(
    @Size(min = 2, max = 160) String companyName,
    @NotNull BusinessType businessType,
    @NotNull MusicProvider musicProvider
) { }
