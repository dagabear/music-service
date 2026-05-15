package kz.genvibe.media_management.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JingleVoice {
    RACHEL("Rachel", "Calm & Clear", "Xb7hH8MSUJpSbSDYk0k2"),
    DOMI("Domi", "Warm & Friendly", "cgSgspJ2msm6clMCkdW9"),
    BELLA("Bella", "Soft & Natural", "hpp4J3VqNfWAUOO0d1Us"),
    ANTONI("Antoni", "Deep & Smooth", "nPczCjzI2devNBz1zQrb");

    private final String name;
    private final String description;
    private final String elevenlabsVoiceId;
}
