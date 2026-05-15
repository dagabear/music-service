package kz.genvibe.media_management.config.props;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "app")
@Data
@Validated
public class AppProps {

    @NotBlank
    private String baseUrl;

    @NotNull
    private VerificationTokenProperties verificationToken;

    @NotNull
    private FileStorageProperties fileStorage;

    public record VerificationTokenProperties(
        @NotNull Duration expiration
    ) {}

    public record FileStorageProperties(
        @NotBlank String uploadDir
    ) {}
}

