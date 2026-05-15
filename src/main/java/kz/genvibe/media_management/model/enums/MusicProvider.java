package kz.genvibe.media_management.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MusicProvider {
    PERSONAL_SPOTIFY_APPLE_MUSIC("Personal Spotify / Apple Music"),
    COMMERCIAL_STREAMING_SERVICE("Commercial Streaming Service"),
    LOCAL_RADIO_CDS("Local Radio / CDs"),
    NO_MUSIC_CURRENTLY("No music currently"),
    CUSTOM_CURATED_SERVICE("Custom Curated Service (Professional Agency)");

    private final String name;
}