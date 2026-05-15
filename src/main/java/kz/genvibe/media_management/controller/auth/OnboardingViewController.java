package kz.genvibe.media_management.controller.auth;

import kz.genvibe.media_management.model.domain.OnboardingSession;
import kz.genvibe.media_management.model.domain.dto.onboarding.Step1Dto;
import kz.genvibe.media_management.model.enums.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/onboarding")
@RequiredArgsConstructor
public class OnboardingViewController {

    private final OnboardingSession session;

    @GetMapping("/welcome")
    public String onboardingStep1(Model model) {
        model.addAttribute("step1Dto", new Step1Dto(null, null, null));
        model.addAttribute("businessTypes", BusinessType.values());
        model.addAttribute("musicProviders", MusicProvider.values());
        return "pages/auth/onboarding/welcome";
    }

    @GetMapping("/brand-identity")
    public String onboardingStep2(Model model) {
        model.addAttribute("brandIdentities", MusicAtmosphere.values());
        return "pages/auth/onboarding/brand-identity";
    }

    @GetMapping("/current-feel")
    public String onboardingStep3(Model model) {
        model.addAttribute("currentFeels", CurrentFeel.values());
        return "pages/auth/onboarding/current-feel";
    }

    @GetMapping("/customer-feel")
    public String onboardingStep4(Model model) {
        model.addAttribute("spacePurposes", MusicMood.values());
        return "pages/auth/onboarding/customer-feel";
    }

    @GetMapping("/playtime-window")
    public String onboardingStep5(Model model) {
        model.addAttribute("playtimeWindows", PlaytimeWindow.values());
        return "pages/auth/onboarding/playtime-window";
    }

    @GetMapping("/verify-email")
    public String verifyEmailPage(Model model) {
        model.addAttribute("companyName", session.getCompanyName());
        return "pages/auth/onboarding/verify-email";
    }

    @GetMapping("/verification-sent")
    public String verificationSentPage() {
        return "pages/auth/onboarding/verification-sent";
    }

}
