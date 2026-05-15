package kz.genvibe.media_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GONE)
public class VerificationLinkExpiredException extends RuntimeException {
    public VerificationLinkExpiredException() {
        super("Email verification link expired");
    }
}
