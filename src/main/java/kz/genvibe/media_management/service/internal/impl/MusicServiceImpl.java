package kz.genvibe.media_management.service.internal.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.genvibe.media_management.model.entity.Music;
import kz.genvibe.media_management.model.entity.Organization;
import kz.genvibe.media_management.repository.MusicRepository;
import kz.genvibe.media_management.service.internal.MusicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MusicServiceImpl implements MusicService {

    private final MusicRepository musicRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Music> getAllDistinctMusic() {
        return musicRepository.findDistinctBy();
    }

    @Override
    @Transactional(readOnly = true)
    public String getMusicPreviewUrl(Organization organization) {
        var music = musicRepository.findTopByAtmosphereAndMood(
            organization.getMusicAtmosphere(),
            organization.getMusicMood()
        ).orElseThrow(() -> new EntityNotFoundException("Cannot find music to play"));

        return music.getFileUrl();
    }

}
