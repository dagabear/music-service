package kz.genvibe.media_management.model.domain.dto.jingle;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record JingleAddStoresDto(
    @NotEmpty List<String> storeNames
) { }
