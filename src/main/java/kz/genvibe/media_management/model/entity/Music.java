package kz.genvibe.media_management.model.entity;

import jakarta.persistence.*;
import kz.genvibe.media_management.model.entity.base.BaseEntity;
import kz.genvibe.media_management.model.enums.MusicAtmosphere;
import kz.genvibe.media_management.model.enums.MusicMood;
import lombok.*;

@Entity
@Table(name = "musics")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Music extends BaseEntity {

    @Column(nullable = false)
    private String fileUrl;

    @Column
    @Enumerated(EnumType.STRING)
    private MusicMood mood;

    @Column
    @Enumerated(EnumType.STRING)
    private MusicAtmosphere atmosphere;

}
