package kz.genvibe.media_management.controller.dashboard;

import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.Store;
import kz.genvibe.media_management.repository.analytics.StoreAnalyticsRepository;
import kz.genvibe.media_management.service.internal.AnalyticsService;
import kz.genvibe.media_management.service.internal.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final AnalyticsService analyticsService;
    private final MusicService musicService;
    private final StoreAnalyticsRepository storeAnalyticsRepository;

    @GetMapping
    public String dashboard(Model model, @CurrentUser AppUser appUser) {
        final var stores = appUser.getOrganization().getStores();
        final var activeStores = stores.stream()
            .filter(Store::isActive)
            .collect(Collectors.toSet());
        activeStores.forEach(it -> it.setLastAccessDate(storeAnalyticsRepository.getLatestSnapshotDate(it.getName())));
        final var organization = appUser.getOrganization();

        model.addAttribute("organization", organization);
        model.addAttribute("stores", stores);
        model.addAttribute("activeStores", activeStores);
        model.addAttribute("dailyPlaysAndMinutes", analyticsService.collectMusicData(organization.getId()));
        model.addAttribute("musicPreviewUrl", musicService.getMusicPreviewUrl(organization));

        return organization.hasEnhancedDashboardView() ? "pages/enhanced-dashboard" : "pages/dashboard";
    }

}
