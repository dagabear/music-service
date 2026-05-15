package kz.genvibe.media_management.controller.store;

import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.repository.analytics.StoreAnalyticsRepository;
import kz.genvibe.media_management.service.internal.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/stores")
@RequiredArgsConstructor
@Slf4j
public class StoreController {

    private final StoreService storeService;
    private final StoreAnalyticsRepository storeAnalyticsRepository;

    @GetMapping
    public String storesPage(
        Model model,
        @CurrentUser AppUser appUser
    ) {
        final var stores = storeService.getAllStores(appUser);
        stores.forEach(it -> {
            if (it.isActive()) it.setLastAccessDate(storeAnalyticsRepository.getLatestSnapshotDate(it.getName()));
        });

        model.addAttribute("stores", stores);
        model.addAttribute("organization", appUser.getOrganization());
        return "pages/stores";
    }

    @GetMapping("/{id}/{uuid}")
    public String storePage(
        @PathVariable long id,
        @PathVariable UUID uuid,
        @CurrentUser AppUser appUser,
        Model model
    ) {
        if (!storeService.verifyStore(id, uuid, appUser)) {
            return "error/404";
        }
        final var store = storeService.getStoreById(id);

        model.addAttribute("store", store);
        model.addAttribute("activeJingles", store.getJingles());
        model.addAttribute("organization", appUser.getOrganization());
        return "pages/store-dashboard";
    }

}
