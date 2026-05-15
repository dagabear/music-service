package kz.genvibe.media_management.client.elevenlabs;

import kz.genvibe.media_management.client.dto.request.ElevenlabsTtsRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface ElevenlabsClient {

    @PostExchange("/text-to-speech/{voiceId}")
    byte[] textToSpeech(@PathVariable String voiceId, @RequestBody ElevenlabsTtsRequest request);

}
