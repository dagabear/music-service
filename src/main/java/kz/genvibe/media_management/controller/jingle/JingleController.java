package kz.genvibe.media_management.controller.jingle;

import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.enums.JingleCategory;
import kz.genvibe.media_management.model.enums.JingleRepeatingTime;
import kz.genvibe.media_management.model.enums.JingleVoice;
import kz.genvibe.media_management.service.internal.JingleService;
import kz.genvibe.media_management.service.internal.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jingles")
@RequiredArgsConstructor
public class JingleController {

    private final StoreService storeService;
    private final JingleService jingleService;

    @GetMapping
    public String jinglesPage(
        @CurrentUser AppUser appUser,
        Model model
    ) {
        model.addAttribute("jingleVoice", JingleVoice.values());
        model.addAttribute("jingleCategory", JingleCategory.values());
        model.addAttribute("jingleRepeatingTime", JingleRepeatingTime.values());
        model.addAttribute("jingleHistory", jingleService.getJingleHistory(appUser));
        model.addAttribute("jingleRequestsToPause", jingleService.getJingleRequestsToPause(appUser));
        model.addAttribute("activeStores", storeService.getAllActiveStores(appUser));
        model.addAttribute("organization", appUser.getOrganization());
        return "pages/jingles";
    }

}
