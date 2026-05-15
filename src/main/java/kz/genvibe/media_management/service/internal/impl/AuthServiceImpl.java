package kz.genvibe.media_management.service.internal.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.genvibe.media_management.config.props.AppProps;
import kz.genvibe.media_management.exception.VerificationLinkExpiredException;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.Organization;
import kz.genvibe.media_management.model.entity.Store;
import kz.genvibe.media_management.model.enums.UserRole;
import kz.genvibe.media_management.repository.AppUserRepository;
import kz.genvibe.media_management.service.internal.AuthService;
import kz.genvibe.media_management.service.internal.EmailVerificationTokenService;
import kz.genvibe.media_management.service.internal.MailService;
import kz.genvibe.media_management.service.internal.OrganizationService;
import kz.genvibe.media_management.service.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private static final String VERIFICATION_URL_PATH = "/auth/verify-email?token=";
    private static final String CONFIRMATION_URL_PATH = "/auth/confirm-email?token=";

    private final OrganizationService organizationService;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final MailService mailService;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppProps appProps;
    private final TemplateEngine templateEngine;

    @Override
    @Transactional
    public void sendEmailVerification(String email) {
        AppUser appUser;
        String verificationLink;
        var emailVerificationToken = emailVerificationTokenService.generate();

        if (appUserRepository.existsByEmail(email)) {
            appUser = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found to verify"));

            if (appUser.isEmailVerified()) throw new IllegalStateException("Email already verified");

            verificationLink = appProps.getBaseUrl() + CONFIRMATION_URL_PATH + emailVerificationToken.getToken();
        } else {
            var organization = organizationService.createOrganizationFromOnboarding(email);

            appUser = AppUser.builder()
                .email(email)
                .role(UserRole.ROLE_ADMIN)
                .organization(organization)
                .build();

            verificationLink = appProps.getBaseUrl() + VERIFICATION_URL_PATH + emailVerificationToken.getToken();
        }
        appUser.setEmailVerificationToken(emailVerificationToken);

        appUserRepository.save(appUser);
        sendEmail(verificationLink, email);

        log.info("Sent email verification to {}", email);
    }

    @Override
    @Transactional
    public void sendStoreEmailVerification(Store store) {
        var emailVerificationToken = emailVerificationTokenService.generate();
        var verificationLink = appProps.getBaseUrl() + VERIFICATION_URL_PATH + emailVerificationToken.getToken();
        var password = PasswordUtil.generateRandomPassword(9);

        var storeUser = store.getStoreUser();

        storeUser.setEnabled(true);
        storeUser.setPassword(passwordEncoder.encode(password));
        storeUser.setEmailVerificationToken(emailVerificationToken);

        sendEmail(verificationLink, storeUser.getEmail(), password);

        log.info("Sent store email verification to {}", storeUser.getEmail());
    }

    @Override
    @Transactional
    public AppUser verifyEmail(String token) {
        var emailVerificationToken = emailVerificationTokenService.findByToken(token)
            .orElseThrow(() -> new EntityNotFoundException("Verification token not found: " + token));

        if (emailVerificationToken.isExpired()) throw new VerificationLinkExpiredException();

        var appUser = emailVerificationToken.getAppUser();
        appUser.setEmailVerified(true);
        appUser.setEmailVerificationToken(null);

        emailVerificationTokenService.delete(emailVerificationToken);

        log.info("Email verified for: {}", appUser.getEmail());

        return appUser;
    }

    @Override
    public void authenticate(AppUser appUser, HttpServletRequest request, HttpServletResponse response) {
        var authentication = new UsernamePasswordAuthenticationToken(
            appUser.getEmail(),
            null,
            List.of(appUser.getRole())
        );
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        new HttpSessionSecurityContextRepository().saveContext(context, request, response);

        log.info("Authenticated user with email: {}", appUser.getEmail());
    }

    private void sendEmail(String verificationUrl, String email) {
        var subject = "Email Address Verification";

        var context = new Context();
        context.setVariable("verificationUrl", verificationUrl);
        var html = templateEngine.process("pages/email/verify-email", context);

        mailService.sendHtmlMail(email, subject, html);
    }

    private void sendEmail(String verificationUrl, String email, String password) {
        var subject = "Email Address Verification";

        var context = new Context();
        context.setVariable("verificationUrl", verificationUrl);
        context.setVariable("password", password);
        var html = templateEngine.process("pages/email/verify-store-email", context);

        mailService.sendHtmlMail(email, subject, html);
    }

}
