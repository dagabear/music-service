package kz.genvibe.media_management.config;

import kz.genvibe.media_management.config.props.AppProps;
import kz.genvibe.media_management.model.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig implements WebMvcConfigurer {

    private final CustomAuthenticationSuccessHandler successHandler;
    private final CurrentUserArgumentResolver currentUserArgumentResolver;
    private final AppProps appProps;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**")
            .addResourceLocations("file:" + appProps.getFileStorage().uploadDir());
    }

    @Override
    public void addArgumentResolvers(@NonNull List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserArgumentResolver);
    }

    @Bean
    public SecurityFilterChain filterChain(
        HttpSecurity http,
        SpringSessionBackedSessionRegistry<? extends Session> sessionRegistry
    ) {
        http
            .csrf(csrf -> csrf.ignoringRequestMatchers("/ws-player/**"))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/**",
                    "/onboarding/**",
                    "/assets/**",
                    "/css/**",
                    "/js/**",
                    "/users/finalize",
                    "/ws-player/**",
                    "/files/**"
                ).permitAll()
                .requestMatchers(
                    "/stores/{id}/{uuid}",
                    "/api/jingles/pause-request/{id}"
                ).hasAuthority(UserRole.ROLE_USER.getAuthority())
                .anyRequest().hasAuthority(UserRole.ROLE_ADMIN.name())
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(successHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry)
                .expiredUrl("/auth/login?expired")
            );
        return http.build();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SpringSessionBackedSessionRegistry<? extends Session> sessionRegistry(
        FindByIndexNameSessionRepository<? extends Session> sessionRepository
    ) {
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }

}
