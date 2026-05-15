package kz.genvibe.media_management.service.integration;

import kz.genvibe.media_management.client.dto.request.ElevenlabsTtsRequest;
import kz.genvibe.media_management.client.elevenlabs.ElevenlabsClient;
import kz.genvibe.media_management.config.props.AppProps;
import kz.genvibe.media_management.service.integration.impl.ElevenlabsIntegrationServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ElevenlabsIntegrationServiceTest {

    @Mock
    private ElevenlabsClient client;

    @Mock
    private AppProps appProps;

    @Mock
    private AppProps.FileStorageProperties fileStorageProperties;

    @InjectMocks
    private ElevenlabsIntegrationServiceImpl elevenlabsIntegrationService;

    @BeforeEach
    void setUp() {
        final var request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(appProps.getFileStorage()).thenReturn(fileStorageProperties);
        when(fileStorageProperties.uploadDir()).thenReturn("build/test-uploads");
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    @DisplayName("Успешный кейс - генерация озвучки и сохранение файла")
    void getSpeechFileUrl_Success() {
        final var text = "test text";
        final var voiceId = "voice_123";
        final var mockBytes = "fake mp3 content".getBytes();

        when(client.textToSpeech(eq(voiceId), any(ElevenlabsTtsRequest.class))).thenReturn(mockBytes);

        final var resultUrl = elevenlabsIntegrationService.getSpeechFileUrl(text, voiceId);

        assertNotNull(resultUrl);
        assertTrue(resultUrl.contains("build/test-uploads"));
        assertTrue(resultUrl.endsWith(".mp3"));

        final var fileName = resultUrl.substring(resultUrl.lastIndexOf("/") + 1);
        final var expectedPath = Paths.get("build/test-uploads").resolve(fileName);
        assertTrue(Files.exists(expectedPath), "Файл должен быть создан на диске");

        try { Files.deleteIfExists(expectedPath); } catch (Exception ignored) {}
    }

}
