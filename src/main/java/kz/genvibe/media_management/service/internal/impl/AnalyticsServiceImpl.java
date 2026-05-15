package kz.genvibe.media_management.service.internal.impl;

import jakarta.persistence.EntityManager;
import kz.genvibe.media_management.model.entity.analytics.*;
import kz.genvibe.media_management.repository.StoreRepository;
import kz.genvibe.media_management.repository.analytics.*;
import kz.genvibe.media_management.service.internal.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {

    private final JingleAggregateAnalyticsDataRepository jingleAggregateAnalyticsDataRepository;
    private final MusicAnalyticsDataRepository musicAnalyticsDataRepository;
    private final StoreAggregateAnalyticsDataRepository storeAggregateAnalyticsDataRepository;
    private final StoreAnalyticsRepository storeAnalyticsRepository;
    private final JingleTypeDistributionDataRepository jingleTypeDistributionDataRepository;
    private final StoreRepository storeRepository;
    private final EntityManager em;

    @Override
    @Transactional
    public void recordPing(long storeId) {
        final var store = storeRepository.findById(storeId).orElseThrow();
        final var today = LocalDate.now();

        final var stats = storeAnalyticsRepository
            .findByStoreNameAndSnapshotDate(store.getName(), today)
            .orElseGet(() -> {
                final var newData = new StoreAnalyticsData();
                newData.setSnapshotDate(today);
                newData.setStoreName(store.getName());
                newData.setPlayMinutes(0);
                return newData;
            });

        stats.setPlayMinutes(stats.getPlayMinutes() + 1);

        storeAnalyticsRepository.save(stats);
    }

    @Override
    public JingleAggregateAnalyticsData collectJingleAggregateData(long organizationId) {
        return jingleAggregateAnalyticsDataRepository.findTopByOrganizationIdAndSnapshotDate(organizationId, LocalDate.now());
    }

    @Override
    public MusicAnalyticsData collectMusicData(long organizationId) {
        return musicAnalyticsDataRepository.findTopByOrganizationIdAndSnapshotDate(organizationId, LocalDate.now());
    }

    @Override
    public StoreAggregateAnalyticsData collectStoreAggregateData(long organizationId) {
        return storeAggregateAnalyticsDataRepository.findTopByOrganizationIdAndSnapshotDate(organizationId, LocalDate.now());
    }

    @Override
    public JingleTypeDistributionData collectJingleDistributionData(long organizationId) {
        return jingleTypeDistributionDataRepository.findTopByOrganizationIdAndSnapshotDate(organizationId, LocalDate.now());
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void refreshAllAnalyticsDataView() {
        em.createNativeQuery("refresh materialized view concurrently music_analytics_data_view").executeUpdate();
        em.createNativeQuery("refresh materialized view concurrently jingle_aggregate_analytics_data_view").executeUpdate();
        em.createNativeQuery("refresh materialized view concurrently store_aggregate_analytics_data_view").executeUpdate();
        em.createNativeQuery("refresh materialized view concurrently jingle_types_distribution_data_view").executeUpdate();
    }

}
