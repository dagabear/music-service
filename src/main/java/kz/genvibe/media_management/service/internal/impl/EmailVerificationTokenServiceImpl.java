package kz.genvibe.media_management.service.internal.impl;

import kz.genvibe.media_management.config.props.AppProps;
import kz.genvibe.media_management.model.entity.EmailVerificationToken;
import kz.genvibe.media_management.repository.EmailVerificationTokenRepository;
import kz.genvibe.media_management.service.internal.EmailVerificationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationTokenServiceImpl implements EmailVerificationTokenService {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final AppProps appProps;

    @Override
    @Transactional
    public EmailVerificationToken generate() {
        var token = UUID.randomUUID().toString();
        var expiry = Instant.now().plus(appProps.getVerificationToken().expiration());
        var emailVerificationToken = new EmailVerificationToken(token, expiry);

        emailVerificationTokenRepository.save(emailVerificationToken);
        log.info("Generated email verification token: {}", token);

        return emailVerificationToken;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmailVerificationToken> findByToken(String token) {
        return emailVerificationTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public void delete(EmailVerificationToken emailVerificationToken) {
        emailVerificationTokenRepository.delete(emailVerificationToken);
    }

}
