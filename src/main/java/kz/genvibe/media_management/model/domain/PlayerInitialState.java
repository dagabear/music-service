package kz.genvibe.media_management.model.domain;

import kz.genvibe.media_management.model.domain.dto.jingle.JingleSlotDto;

import java.util.List;

public record PlayerInitialState(
    List<String> playlistUrls,
    List<JingleSlotDto> dailySlots,
    String organizationName
) {}