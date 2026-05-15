package kz.genvibe.media_management.model.domain.dto.analytics;

public record AggregateJinglePerformanceDto(
    int totalJingleBroadcasts,
    int totalJingleBroadcastsWeekGrowth,
    int completionRate,
    String mostPlayedJingleName,
    int mostPlayedJinglePlayCount,
    String scheduledJingleTaskName,
    int scheduledJingleTaskCount
) { }
