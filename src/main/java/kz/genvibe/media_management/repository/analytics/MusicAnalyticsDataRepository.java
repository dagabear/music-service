package kz.genvibe.media_management.repository.analytics;

import kz.genvibe.media_management.model.entity.analytics.MusicAnalyticsData;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;

public interface MusicAnalyticsDataRepository extends Repository<MusicAnalyticsData, Long> {
    MusicAnalyticsData findTopByOrganizationIdAndSnapshotDate(long organizationId, LocalDate snapshotDate);
}
