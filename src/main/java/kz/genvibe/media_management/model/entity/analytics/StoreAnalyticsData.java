package kz.genvibe.media_management.model.entity.analytics;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kz.genvibe.media_management.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "store_analytics_data")
public class StoreAnalyticsData extends BaseEntity {

    @Column
    private LocalDate snapshotDate;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private int playMinutes;

}
