package kz.genvibe.media_management.repository;

import kz.genvibe.media_management.model.entity.Music;
import kz.genvibe.media_management.model.enums.MusicAtmosphere;
import kz.genvibe.media_management.model.enums.MusicMood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MusicRepository extends JpaRepository<Music, Long> {
    List<Music> findDistinctBy();
    Optional<Music> findTopByAtmosphereAndMood(MusicAtmosphere atmosphere, MusicMood mood);

    List<Music> findAllByAtmosphereAndMood(MusicAtmosphere atmosphere, MusicMood mood);
}
