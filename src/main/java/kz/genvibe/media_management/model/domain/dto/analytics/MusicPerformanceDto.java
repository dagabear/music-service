package kz.genvibe.media_management.model.domain.dto.analytics;

import java.util.Map;

public record MusicPerformanceDto(
    Map<String, Integer> weeklyPlays,
    int totalPlays,
    int totalPlaysGrowthPercentage,
    int averageDailyPlays,
    String topStoreName,
    int topStorePlaysGrowthPercentage
) { }
