package kz.genvibe.media_management.model.domain.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordSetupDto(
    @Email String email,

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(regexp = ".*[0-9].*", message = "Password must contain at least one digit")
    String password,

    @NotBlank(message = "Please confirm your password")
    String confirmPassword
) {
    public boolean passwordMatches() {
        return password.equals(confirmPassword);
    }
}
