package kz.genvibe.media_management.model.domain;

import kz.genvibe.media_management.model.enums.CommandType;

public record PlayerCommand(
    CommandType type,
    String jingleUrl,
    long slotId,
    double volume
) { }
