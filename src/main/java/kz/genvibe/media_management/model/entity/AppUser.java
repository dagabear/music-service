package kz.genvibe.media_management.model.entity;

import jakarta.persistence.*;
import kz.genvibe.media_management.model.entity.base.UpdateEntity;
import kz.genvibe.media_management.model.enums.UserRole;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUser extends UpdateEntity {

    @Column(unique = true, length = 254, nullable = false)
    private String email;

    @Column
    private String password;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private UserRole role = UserRole.ROLE_USER;

    @Builder.Default
    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean emailChanged = false;

    @Builder.Default
    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean emailVerified = false;

    @Builder.Default
    @Column(columnDefinition = "boolean default true", nullable = false)
    private boolean enabled = true;

    @Column
    private String fullName;

    @Column
    private String companyRole;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "email_verification_token_id")
    private EmailVerificationToken emailVerificationToken;

    @ManyToOne
    @JoinColumn(name = "organization_id", updatable = false, nullable = false)
    private Organization organization;

}
