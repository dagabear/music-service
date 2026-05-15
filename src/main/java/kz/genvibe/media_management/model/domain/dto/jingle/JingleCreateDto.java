package kz.genvibe.media_management.model.domain.dto.jingle;

import jakarta.validation.constraints.*;
import kz.genvibe.media_management.model.entity.Jingle;
import kz.genvibe.media_management.model.enums.JingleCategory;
import kz.genvibe.media_management.model.enums.JingleRepeatingTime;
import kz.genvibe.media_management.model.enums.JingleVoice;

import java.time.LocalDateTime;

public record JingleCreateDto(
    @NotNull JingleVoice voice,
    @NotNull JingleCategory category,
    @NotNull LocalDateTime startDate,
    @NotNull @Future LocalDateTime endDate,
    @NotNull JingleRepeatingTime repeatingTime,
    @NotBlank @Size(min = 1, max = 500) String announcementText,
    @NotNull
    @DecimalMin("0.7")
    @DecimalMax("1.2")
            Double speed
) {
    public Jingle toEntity() {
        return Jingle.builder()
            .voice(voice)
            .category(category)
            .startDate(startDate)
            .endDate(endDate)
            .repeatingTime(repeatingTime)
            .announcementText(announcementText)
            .speed(speed != null ? speed : 1.0)
            .build();
    }
}
