package kz.genvibe.media_management.service.internal.impl;

import kz.genvibe.media_management.repository.AppUserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @NonNull
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        var user = appUserRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        var isEnabled = user.isEnabled() && user.isEmailVerified();

        return User.withUsername(username)
            .password(user.getPassword())
            .disabled(!isEnabled)
            .accountExpired(false)
            .credentialsExpired(false)
            .accountLocked(false)
            .authorities(user.getRole())
            .build();
    }

}