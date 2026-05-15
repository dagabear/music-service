package kz.genvibe.media_management.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import kz.genvibe.media_management.model.entity.base.CreateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "email_verification_tokens")
@Getter
@Setter
@NoArgsConstructor
public class EmailVerificationToken extends CreateEntity {

    @Column(nullable = false)
    private String token;

    @Column(updatable = false, nullable = false)
    private Instant expiryDate;

    @OneToOne(mappedBy = "emailVerificationToken")
    private AppUser appUser;

    public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }

    public EmailVerificationToken(String token, Instant expiryDate) {
        this.token = token;
        this.expiryDate = expiryDate;
    }

}
