package kz.genvibe.media_management.service.internal.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.genvibe.media_management.exception.JingleCreationLimitExceededException;
import kz.genvibe.media_management.model.domain.PlayerCommand;
import kz.genvibe.media_management.model.domain.dto.jingle.JingleCreateDto;
import kz.genvibe.media_management.model.entity.*;
import kz.genvibe.media_management.model.enums.CommandType;
import kz.genvibe.media_management.model.enums.JingleSlotStatus;
import kz.genvibe.media_management.repository.JingleRepository;
import kz.genvibe.media_management.repository.JingleSlotRepository;
import kz.genvibe.media_management.service.integration.ElevenlabsIntegrationService;
import kz.genvibe.media_management.service.internal.JingleService;
import kz.genvibe.media_management.service.internal.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JingleServiceImpl implements JingleService {

    private final StoreService storeService;
    private final ElevenlabsIntegrationService elevenlabsIntegrationService;
    private final JingleRepository jingleRepository;
    private final JingleSlotRepository jingleSlotRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public void createJingle(AppUser appUser, JingleCreateDto dto) {
        var organization = appUser.getOrganization();
        var jinglesCount = jingleRepository.countAllByOrganization(organization);

        if (jinglesCount >= 20) {
            throw new JingleCreationLimitExceededException("You exceeded the limit of 20");
        }

        var speechFileUrl = elevenlabsIntegrationService.getSpeechFileUrl(
            dto.announcementText(),
            dto.voice().getElevenlabsVoiceId()
        );

        var jingle = dto.toEntity();
        jingle.setOrganization(organization);
        jingle.setFileUrl(speechFileUrl);

        jingleRepository.save(jingle);

        log.info("Jingle created for organization: {}", appUser.getOrganization().getCompanyName());
    }

    @Override
    @Transactional
    public void deleteJingleById(long id, AppUser appUser) {
        var jingle = jingleRepository.findJingleByIdAndOrganization(id, appUser.getOrganization())
                .orElseThrow(() -> new EntityNotFoundException("Jingle not found"));
        jingleRepository.deleteByIdAndOrg(id, appUser.getOrganization());
    }

    @Override
    @Transactional
    public void setPauseApprovalStatus(
        long id,
        AppUser appUser
    ) {
        var jingle = jingleRepository.findJingleByIdAndOrganization(id, appUser.getOrganization())
            .orElseThrow(() -> new EntityNotFoundException("Jingle not found"));
        jingle.setPauseApproved(true);
    }

    @Override
    @Transactional
    public void addJingleToStores(
        long id,
        List<Long> idList,
        AppUser appUser
    ) {
        var jingle = jingleRepository.findJingleByIdAndOrganization(id, appUser.getOrganization())
            .orElseThrow(() -> new EntityNotFoundException("Jingle not found"));
        var stores = storeService.getAllStoresByAppUserAndIdList(appUser, idList);

        jingle.getStores().addAll(stores);

        final var jingleSchedules = stores.stream()
            .map(Store::getJingleSchedule)
            .collect(Collectors.toSet());

        generateSlotsForJingle(jingle, jingleSchedules);
    }

    @Override
    @Transactional
    public void requestToPauseJingle(long id) {
        var jingle = jingleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Jingle not found"));
        jingle.setRequestedToPause(true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Jingle> getJingleHistory(AppUser appUser) {
        return jingleRepository.findJinglesByOrganization(appUser.getOrganization());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Jingle> getJingleRequestsToPause(AppUser appUser) {
        return jingleRepository.findJinglesByOrganizationAndRequestedToPauseIsTrue(appUser.getOrganization());
    }

    private void generateSlotsForJingle(Jingle jingle, Set<JingleSchedule> schedules) {
        var now = LocalDateTime.now().withSecond(0).withNano(0);

        if (now.isAfter(jingle.getEndDate()) || now.plusDays(1).isBefore(jingle.getStartDate())) {
            return;
        }

        var minutesInterval = jingle.getRepeatingTime().getDuration().toMinutes();
        var nextSlotTime = jingle.getStartDate().isAfter(now) ? jingle.getStartDate() : now;
        var endOfDay = now.toLocalDate().atTime(LocalTime.MAX);

        for (final var schedule: schedules) {
            while (nextSlotTime.isBefore(endOfDay) && nextSlotTime.isBefore(jingle.getEndDate())) {
                var slot = JingleSlot.builder()
                    .jingle(jingle)
                    .jingleSchedule(schedule)
                    .playTime(nextSlotTime)
                    .status(JingleSlotStatus.PENDING)
                    .build();

                schedule.addSlot(slot);
                nextSlotTime = nextSlotTime.plusMinutes(minutesInterval);
            }
        }
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void checkAndBroadcastJingles() {
        log.info("Starting broadcast");
        var now = LocalDateTime.now().withSecond(0).withNano(0);
        var currentSlots = jingleSlotRepository.findJingleSlotsByPlayTimeAndStatusAndJingleRequestedToPauseIsFalse(
            now,
            JingleSlotStatus.PENDING
        );

        for (var slot : currentSlots) {
            var storeId = slot.getJingleSchedule().getStore().getId();

            var command = new PlayerCommand(
                CommandType.PLAY_JINGLE,
                slot.getJingle().getFileUrl(),
                slot.getId(),
                slot.getJingle().getSpeed()
            );

            messagingTemplate.convertAndSend("/topic/store." + storeId + ".commands", command);

            slot.setStatus(JingleSlotStatus.PLAYED);
        }

        if (!currentSlots.isEmpty()) {
            log.info("Broadcasted {} jingles at {}", currentSlots.size(), now);
        }
    }

}
