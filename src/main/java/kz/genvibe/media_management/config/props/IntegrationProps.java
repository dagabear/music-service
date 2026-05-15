package kz.genvibe.media_management.config.props;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "integration")
@Data
@Validated
public class IntegrationProps {

    @NotNull
    private ElevenlabsProperties elevenlabs;

    public record ElevenlabsProperties(
        @NotBlank String baseUrl,
        @NotBlank String apiKey
    ) {}

}
