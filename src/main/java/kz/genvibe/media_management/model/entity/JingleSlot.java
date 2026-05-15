package kz.genvibe.media_management.model.entity;

import jakarta.persistence.*;
import kz.genvibe.media_management.model.entity.base.CreateEntity;
import kz.genvibe.media_management.model.enums.JingleSlotStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jingle_slots")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JingleSlot extends CreateEntity {

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime playTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "jingle_id", nullable = false)
    private Jingle jingle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "jingle_schedule_id", nullable = false)
    private JingleSchedule jingleSchedule;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private JingleSlotStatus status = JingleSlotStatus.PENDING;

}
