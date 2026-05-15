package kz.genvibe.media_management.model.entity.analytics;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kz.genvibe.media_management.model.entity.base.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;

@Entity
@Table(name = "store_aggregate_analytics_data_view")
@Getter
@Immutable
public class StoreAggregateAnalyticsData extends BaseEntity {
    private LocalDate snapshotDate;
    private long organizationId;
    private int totalJingleBroadcasts;
    private int totalJingleBroadcastsWeekGrowth;
    private int completionRate;
    private String mostPlayedJingleName;
    private int mostPlayedJinglePlayCount;
    private String scheduledJingleTaskName;
    private int scheduledJingleTaskCount;
}
