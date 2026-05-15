package kz.genvibe.media_management.model.domain.dto.analytics;

import java.util.List;

public record StorePerformanceDto(
    List<DailyStorePerformance> dailyStorePerformances,
    String dailySessionActivity
) {

    public record DailyStorePerformance(
        String storeName,
        int playMinutes
    ) {}

}
