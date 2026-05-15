package kz.genvibe.media_management.model.entity;

import jakarta.persistence.*;
import kz.genvibe.media_management.model.entity.base.CreateEntity;
import kz.genvibe.media_management.model.enums.JingleCategory;
import kz.genvibe.media_management.model.enums.JingleRepeatingTime;
import kz.genvibe.media_management.model.enums.JingleVoice;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jingles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Jingle extends CreateEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JingleVoice voice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JingleCategory category;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JingleRepeatingTime repeatingTime;

    @Column(length = 500, nullable = false, updatable = false)
    private String announcementText;

    @Column(nullable = false)
    @Builder.Default
    private Double speed = 1.0;

    @Column(nullable = false)
    private String fileUrl;

    @Builder.Default
    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean requestedToPause = false;

    @Builder.Default
    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean pauseApproved = false;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Builder.Default
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "jingle_stores",
        joinColumns = @JoinColumn(name = "jingle_id"),
        inverseJoinColumns = @JoinColumn(name = "store_id")
    )
    private Set<Store> stores = new HashSet<>();

}
