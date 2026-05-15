package kz.genvibe.media_management.model.entity.analytics;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import kz.genvibe.media_management.model.entity.base.BaseEntity;
import kz.genvibe.media_management.model.enums.JingleCategory;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "jingle_types_distribution_data_view")
public class JingleTypeDistributionData extends BaseEntity {
    private LocalDate snapshotDate;
    private long organizationId;
    @Enumerated(EnumType.STRING)
    private JingleCategory category;
    private int totalPlays;
    private int percentage;
}
