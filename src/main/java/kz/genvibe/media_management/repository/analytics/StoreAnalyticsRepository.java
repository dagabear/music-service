package kz.genvibe.media_management.repository.analytics;

import kz.genvibe.media_management.model.entity.analytics.StoreAnalyticsData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface StoreAnalyticsRepository extends JpaRepository<StoreAnalyticsData, Long> {
    Optional<StoreAnalyticsData> findByStoreNameAndSnapshotDate(String storeName, LocalDate snapshotDate);

    @Query(value = "select max(snapshot_date) from store_analytics_data where store_name=:storeName", nativeQuery = true)
    LocalDate getLatestSnapshotDate(@Param("storeName") String storeName);
}
