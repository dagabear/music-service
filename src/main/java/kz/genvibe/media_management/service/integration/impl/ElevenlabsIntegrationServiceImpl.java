package kz.genvibe.media_management.service.integration.impl;

import kz.genvibe.media_management.client.base.OutgoingRequestService;
import kz.genvibe.media_management.client.dto.request.ElevenlabsTtsRequest;
import kz.genvibe.media_management.client.elevenlabs.ElevenlabsClient;
import kz.genvibe.media_management.config.props.AppProps;
import kz.genvibe.media_management.service.integration.ElevenlabsIntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElevenlabsIntegrationServiceImpl implements ElevenlabsIntegrationService {

    private final OutgoingRequestService outgoingRequestService;
    private final ElevenlabsClient elevenlabsClient;
    private final AppProps appProps;

    @SneakyThrows
    public String getSpeechFileUrl(String text, String voiceId) {
        var requestBody = new ElevenlabsTtsRequest(
            text,
            "eleven_v3"
        );

        var fileBytes = elevenlabsClient.textToSpeech(voiceId, requestBody);
        var fileName = UUID.randomUUID() + ".mp3";
        var directory = Paths.get(appProps.getFileStorage().uploadDir());
        var filePath = directory.resolve(fileName);

        Files.createDirectories(directory);
        Files.write(filePath, fileBytes);

        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(appProps.getFileStorage().uploadDir())
            .path("/")
            .path(fileName)
            .toUriString();
    }

}
