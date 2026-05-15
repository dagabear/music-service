package kz.genvibe.media_management.model.entity;

import jakarta.persistence.*;
import kz.genvibe.media_management.model.entity.base.UpdateEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jingle_schedules")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JingleSchedule extends UpdateEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Builder.Default
    @OneToMany(mappedBy = "jingleSchedule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("playTime asc")
    private List<JingleSlot> dailyJingleSlots = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "boolean default true")
    @Builder.Default
    private boolean active = true;

    public void addSlot(JingleSlot slot) {
        dailyJingleSlots.add(slot);
        slot.setJingleSchedule(this);
    }

    public JingleSchedule(Store store) {
        this.store = store;
    }

}
