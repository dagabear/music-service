package kz.genvibe.media_management.controller.store;

import jakarta.validation.Valid;
import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.domain.dto.store.StoreCreateDto;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.service.internal.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreActionController {

    private final StoreService storeService;

    @PostMapping
    public String createStore(
        @ModelAttribute @Valid StoreCreateDto dto,
        @CurrentUser AppUser appUser,
        RedirectAttributes redirectAttributes
    ) {
        storeService.addStore(appUser, dto);
        redirectAttributes.addFlashAttribute("toast", "Store added successfully");
        return "redirect:/stores";
    }

    @PostMapping("/activate/{id}")
    public String activateStore(
        @PathVariable long id,
        @CurrentUser AppUser appUser
    ) {
        storeService.activateStore(id, appUser);
        return "redirect:/stores";
    }

    @ResponseBody
    @PostMapping("/regenerate-link/{id}")
    public String regenerateLink(
        @PathVariable long id,
        @CurrentUser AppUser appUser
    ) {
        return storeService.regenerateMusicAccessLink(id, appUser);
    }

}
