package kz.genvibe.media_management.model.domain.dto.store;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StoreCreateDto(
    @NotBlank @Size(min = 3, message = "Store name cannot be blank or less than 3 characters")
    String name,
    String location,
    @Email String email
) { }
