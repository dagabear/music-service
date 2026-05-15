package kz.genvibe.media_management.controller.store;

import jakarta.persistence.EntityNotFoundException;
import kz.genvibe.media_management.model.domain.PlayerInitialState;
import kz.genvibe.media_management.model.domain.dto.jingle.JingleSlotDto;
import kz.genvibe.media_management.model.entity.Music;
import kz.genvibe.media_management.model.enums.JingleSlotStatus;
import kz.genvibe.media_management.repository.JingleScheduleRepository;
import kz.genvibe.media_management.repository.MusicRepository;
import kz.genvibe.media_management.repository.OrganizationRepository;
import kz.genvibe.media_management.repository.StoreRepository;
import kz.genvibe.media_management.service.internal.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StoreMusicPlayerController {

    private final AnalyticsService analyticsService;
    private final StoreRepository storeRepository;
    private final OrganizationRepository organizationRepository;
    private final JingleScheduleRepository scheduleRepository;
    private final MusicRepository musicRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/sync.{storeId}")
    public void syncSchedule(@DestinationVariable long storeId) {
        var store = storeRepository.findById(storeId).orElseThrow();
        var org = organizationRepository.findById(store.getOrganization().getId())
            .orElseThrow(() -> new EntityNotFoundException("Organization not found"));

        var musicUrls = musicRepository
            .findAllByAtmosphereAndMood(org.getMusicAtmosphere(), org.getMusicMood())
            .stream()
            .map(Music::getFileUrl)
            .toList();

        var schedule = scheduleRepository.findJingleScheduleByStore(store)
            .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));

        var pendingSlots = schedule.getDailyJingleSlots().stream()
            .filter(slot -> slot.getStatus() == JingleSlotStatus.PENDING)
            .map(slot -> new JingleSlotDto(slot.getId(), slot.getPlayTime(), slot.getJingle().getFileUrl()))
            .toList();

        var initialState = new PlayerInitialState(
            musicUrls,
            pendingSlots,
            org.getCompanyName()
        );

        messagingTemplate.convertAndSend("/topic/store." + storeId + ".init", initialState);
    }

    @MessageMapping("/track.ping.{storeId}")
    public void handleHeartbeat(@DestinationVariable long storeId) {
        analyticsService.recordPing(storeId);
    }

}
