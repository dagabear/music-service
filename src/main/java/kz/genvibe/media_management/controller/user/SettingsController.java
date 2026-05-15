package kz.genvibe.media_management.controller.user;

import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.enums.MusicAtmosphere;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
@RequiredArgsConstructor
public class SettingsController {

    @GetMapping
    public String settingsPage(
        @CurrentUser AppUser appUser,
        Model model
    ) {
        model.addAttribute("appUser", appUser);
        model.addAttribute("organization", appUser.getOrganization());
        model.addAttribute("musicAtmospheres", MusicAtmosphere.values());
        return "pages/settings";
    }

}
