package kz.genvibe.media_management.client.dto.request;

public record ElevenlabsTtsRequest(
    String text,
    String model_id
) { }
