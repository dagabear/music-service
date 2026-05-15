package kz.genvibe.media_management.model.domain.dto.jingle;

import java.time.LocalDateTime;

public record JingleSlotDto(
    long id,
    LocalDateTime playTime,
    String fileUrl
) { }
