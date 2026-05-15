package kz.genvibe.media_management.controller.music;

import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.enums.MusicAtmosphere;
import kz.genvibe.media_management.model.enums.MusicMood;
import kz.genvibe.media_management.service.internal.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/music")
@RequiredArgsConstructor
public class MusicLibraryController {

    private final MusicService musicService;

    @GetMapping
    public String musicPage(Model model, @CurrentUser AppUser appUser) {
        model.addAttribute("musicAtmospheres", MusicAtmosphere.values());
        model.addAttribute("musicMoods", MusicMood.values());
        model.addAttribute("musics", musicService.getAllDistinctMusic());
        model.addAttribute("organization", appUser.getOrganization());
        return "pages/music";
    }

}
