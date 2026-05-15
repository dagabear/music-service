package kz.genvibe.media_management.controller.analytics;

import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.Store;
import kz.genvibe.media_management.service.internal.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping
    public String analyticsPage(
        @CurrentUser AppUser appUser,
        Model model
    ) {
        final var organizationId = appUser.getOrganization().getId();
        final var activeStores = appUser.getOrganization().getStores().stream()
                .filter(Store::isActive)
                .toList();

        model.addAttribute("jingleData", analyticsService.collectJingleAggregateData(organizationId));
        model.addAttribute("musicData", analyticsService.collectMusicData(organizationId));
        model.addAttribute("storeData", analyticsService.collectStoreAggregateData(organizationId));
        model.addAttribute("jingleDistributionData", analyticsService.collectJingleDistributionData(organizationId));
        model.addAttribute("organization", appUser.getOrganization());
        model.addAttribute("activeStores", activeStores);
        model.addAttribute("stores", appUser.getOrganization().getStores());
        return "pages/analytics";
    }

}
