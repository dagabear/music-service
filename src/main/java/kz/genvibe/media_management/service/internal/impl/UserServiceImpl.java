package kz.genvibe.media_management.service.internal.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.genvibe.media_management.exception.UserAlreadyExistsException;
import kz.genvibe.media_management.model.domain.dto.user.AppUserUpdateDto;
import kz.genvibe.media_management.model.domain.dto.user.PasswordSetupDto;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.Organization;
import kz.genvibe.media_management.repository.AppUserRepository;
import kz.genvibe.media_management.service.internal.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AppUser setupUserPassword(
        PasswordSetupDto passwordSetupDto,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        var email = passwordSetupDto.email();
        var appUser = getUserByEmail(email);

        appUser.setPassword(passwordEncoder.encode(passwordSetupDto.password()));

        appUserRepository.save(appUser);

        log.info("Setup password for user with email: {}", email);

        return appUser;
    }

    @Override
    @Transactional
    public void updateUser(AppUserUpdateDto dto, AppUser appUser) {
        appUser.setFullName(dto.fullName());
        appUser.setCompanyRole(dto.companyRole());

        if (!dto.email().equals(appUser.getEmail())) {
            if (existsByEmail(dto.email())) {
                throw new UserAlreadyExistsException("User with email: " + dto.email() + " already exists");
            }
            appUser.setEmail(dto.email());
            appUser.setEmailChanged(true);
            appUser.setEmailVerified(false);
        }

        appUserRepository.save(appUser);

        log.info("User updated with email: {}", appUser.getEmail());
    }

    @Override
    @Transactional
    public AppUser createStoreUser(String email, Organization organization) {
        if (existsByEmail(email)) {
            throw new UserAlreadyExistsException("This email has been already taken");
        }

        var storeUser = AppUser.builder()
            .email(email)
            .organization(organization)
            .enabled(false)
            .build();

        appUserRepository.save(storeUser);

        return storeUser;
    }

    @Override
    @Transactional(readOnly = true)
    public AppUser getUserByEmail(String email) {
        return appUserRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("User with email: " + email + " not found"));
    }

    @Override
    public boolean existsByEmail(String email) {
        return appUserRepository.existsByEmail(email);
    }

}
