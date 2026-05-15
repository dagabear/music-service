package kz.genvibe.media_management.repository.analytics;

import kz.genvibe.media_management.model.entity.analytics.JingleTypeDistributionData;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;

public interface JingleTypeDistributionDataRepository extends Repository<JingleTypeDistributionData, Long> {
    JingleTypeDistributionData findTopByOrganizationIdAndSnapshotDate(long organizationId, LocalDate snapshotDate);
}
