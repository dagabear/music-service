package kz.genvibe.media_management.service.internal;

import kz.genvibe.media_management.model.entity.Music;
import kz.genvibe.media_management.model.entity.Organization;

import java.util.List;

public interface MusicService {
    List<Music> getAllDistinctMusic();
    String getMusicPreviewUrl(Organization organization);
}
