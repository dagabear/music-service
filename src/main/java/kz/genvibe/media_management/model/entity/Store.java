package kz.genvibe.media_management.model.entity;

import jakarta.persistence.*;
import kz.genvibe.media_management.model.entity.base.UpdateEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "stores")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Store extends UpdateEntity {

    @Column(nullable = false)
    private String name;

    @Column
    private String location;

    @Column(length = 254, nullable = false)
    private String email;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean active = false;

    @Column(unique = true)
    private String musicLink;

    @Column(name = "music_link_UUID")
    private UUID musicLinkUuid;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Builder.Default
    @ManyToMany(mappedBy = "stores", fetch = FetchType.EAGER)
    private Set<Jingle> jingles = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private AppUser storeUser;

    @OneToOne(mappedBy = "store")
    private JingleSchedule jingleSchedule;

    @Transient
    private LocalDate lastAccessDate;

}
