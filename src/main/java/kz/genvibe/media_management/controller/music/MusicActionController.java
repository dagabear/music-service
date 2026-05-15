package kz.genvibe.media_management.controller.music;

import jakarta.validation.constraints.NotNull;
import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.enums.MusicAtmosphere;
import kz.genvibe.media_management.model.enums.MusicMood;
import kz.genvibe.media_management.service.internal.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/music")
@RequiredArgsConstructor
public class MusicActionController {

    private final OrganizationService organizationService;

    @PostMapping
    @ResponseBody
    public String saveMusicAtmosphereAndMood(
        @RequestParam @NotNull MusicAtmosphere atmosphere,
        @RequestParam @NotNull MusicMood mood,
        @CurrentUser AppUser appUser,
        RedirectAttributes redirectAttributes
    ) {
        organizationService.saveMusicTypes(appUser, atmosphere, mood);
        redirectAttributes.addFlashAttribute("toast", "Music type successfully saved for your company");
        return "redirect:/dashboard";
    }

}
