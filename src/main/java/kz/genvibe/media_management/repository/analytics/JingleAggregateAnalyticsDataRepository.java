package kz.genvibe.media_management.repository.analytics;

import kz.genvibe.media_management.model.entity.analytics.JingleAggregateAnalyticsData;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;

public interface JingleAggregateAnalyticsDataRepository extends Repository<JingleAggregateAnalyticsData, Long> {
    JingleAggregateAnalyticsData findTopByOrganizationIdAndSnapshotDate(long organizationId, LocalDate snapshotDate);
}
