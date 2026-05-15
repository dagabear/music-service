package kz.genvibe.media_management.model.entity.analytics;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kz.genvibe.media_management.model.entity.base.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Entity
@Table(name = "music_analytics_data_view")
@Immutable
public class MusicAnalyticsData extends BaseEntity {

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Integer> weeklyPlays;

    private LocalDate snapshotDate;
    private long organizationId;
    private int totalPlays;
    private int totalPlaysGrowthPercentage;
    private int averageDailyPlays;
    private String topStoreName;
    private int topStorePlaysGrowthPercentage;
}
