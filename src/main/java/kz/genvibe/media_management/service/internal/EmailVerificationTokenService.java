package kz.genvibe.media_management.service.internal;

import kz.genvibe.media_management.model.entity.EmailVerificationToken;

import java.util.Optional;

public interface EmailVerificationTokenService {
    EmailVerificationToken generate();
    Optional<EmailVerificationToken> findByToken(String token);
    void delete(EmailVerificationToken emailVerificationToken);
}
