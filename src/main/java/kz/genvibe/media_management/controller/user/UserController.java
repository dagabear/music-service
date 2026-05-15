package kz.genvibe.media_management.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.domain.dto.user.AppUserUpdateDto;
import kz.genvibe.media_management.model.domain.dto.user.PasswordSetupDto;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.service.internal.AuthService;
import kz.genvibe.media_management.service.internal.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/finalize")
    public String setupUserPassword(
        @ModelAttribute PasswordSetupDto passwordSetupDto,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        var appUser = userService.setupUserPassword(passwordSetupDto, request, response);
        authService.authenticate(appUser, request, response);
        return "redirect:/dashboard";
    }

    @PatchMapping
    public String updateUser(
        @ModelAttribute AppUserUpdateDto dto,
        @CurrentUser AppUser appUser,
        HttpSession session
    ) {
        var isEmailChanged = !dto.email().equals(appUser.getEmail());
        userService.updateUser(dto, appUser);
        if (isEmailChanged) session.invalidate();

        return isEmailChanged ? "redirect:/auth/confirm?email=" + appUser.getEmail() : "redirect:/settings";
    }

}
