package kz.genvibe.media_management.repository.analytics;

import kz.genvibe.media_management.model.entity.analytics.StoreAggregateAnalyticsData;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;

public interface StoreAggregateAnalyticsDataRepository extends Repository<StoreAggregateAnalyticsData, Long> {
    StoreAggregateAnalyticsData findTopByOrganizationIdAndSnapshotDate(long organizationId, LocalDate snapshotDate);
}
