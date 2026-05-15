package kz.genvibe.media_management.model.domain.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AppUserUpdateDto(
    @NotBlank String fullName,
    @Email String email,
    @NotBlank String companyRole
) { }
