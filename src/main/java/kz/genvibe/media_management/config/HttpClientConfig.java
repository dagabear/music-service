package kz.genvibe.media_management.config;

import kz.genvibe.media_management.client.elevenlabs.ElevenlabsClient;
import kz.genvibe.media_management.config.props.IntegrationProps;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class HttpClientConfig {

    private final IntegrationProps props;

    @Bean
    public ElevenlabsClient elevenlabsClient(RestClient.Builder builder) {
        var restClient = builder
            .baseUrl(props.getElevenlabs().baseUrl())
            .defaultHeader("xi-api-key", props.getElevenlabs().apiKey())
            .defaultHeader("Content-Type", "application/json")
            .build();

        var factory = HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(restClient))
            .build();

        return factory.createClient(ElevenlabsClient.class);
    }

}
