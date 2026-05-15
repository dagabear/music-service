package kz.genvibe.media_management.model.domain.dto.user;

import jakarta.validation.constraints.*;
import kz.genvibe.media_management.model.enums.*;

import java.util.List;

public record AppUserDto(
    @NotBlank @Size(min = 6, max = 20) String password,
    @NotBlank @Email String email,
    @NotBlank @Size(min = 2, max = 160) String companyName,
    @NotNull BusinessType businessType,
    @NotNull MusicProvider musicProvider,
    @NotNull @NotEmpty List<MusicAtmosphere> brandIdentity,
    @NotNull @NotEmpty List<CurrentFeel> currentFeel,
    @NotNull MusicMood spacePurpose,
    @NotNull PlaytimeWindow playtimeWindow
) { }
