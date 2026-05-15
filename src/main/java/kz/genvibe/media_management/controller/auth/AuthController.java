package kz.genvibe.media_management.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Email;
import kz.genvibe.media_management.service.internal.AuthService;
import kz.genvibe.media_management.service.internal.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/register")
    public String register() {
        return "pages/auth/register";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "pages/auth/login";
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return "redirect:/auth/register";
    }

    @GetMapping("/confirm")
    public String confirmEmail(
        @RequestParam @Email String email,
        Model model
    ) {
        model.addAttribute("email", email);
        authService.sendEmailVerification(email);
        return "pages/auth/confirm";
    }

    @GetMapping("/confirm-email")
    public String confirmPage(
        @RequestParam String token,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        var appUser = authService.verifyEmail(token);
        authService.authenticate(appUser, request, response);
        return "pages/auth/confirm-email";
    }

    @PostMapping("/send-email")
    public String sendEmail(@RequestParam String email) {
        authService.sendEmailVerification(email);
        var appUser = userService.getUserByEmail(email);

        return appUser.isEmailChanged()
            ? "redirect:/auth/confirm-email" : "redirect:/onboarding/verification-sent";
    }

}
