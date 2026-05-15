package kz.genvibe.media_management.service.internal;

import kz.genvibe.media_management.model.entity.analytics.JingleAggregateAnalyticsData;
import kz.genvibe.media_management.model.entity.analytics.JingleTypeDistributionData;
import kz.genvibe.media_management.model.entity.analytics.MusicAnalyticsData;
import kz.genvibe.media_management.model.entity.analytics.StoreAggregateAnalyticsData;

public interface AnalyticsService {
    void recordPing(long storeId);
    JingleAggregateAnalyticsData collectJingleAggregateData(long organizationId);
    MusicAnalyticsData collectMusicData(long organizationId);
    StoreAggregateAnalyticsData collectStoreAggregateData(long organizationId);
    JingleTypeDistributionData collectJingleDistributionData(long organizationId);
}
