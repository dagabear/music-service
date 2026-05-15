package kz.genvibe.media_management.service.internal.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.genvibe.media_management.config.props.AppProps;
import kz.genvibe.media_management.model.domain.dto.store.ActiveStoreDto;
import kz.genvibe.media_management.model.domain.dto.store.StoreCreateDto;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.JingleSchedule;
import kz.genvibe.media_management.model.entity.Store;
import kz.genvibe.media_management.repository.JingleScheduleRepository;
import kz.genvibe.media_management.repository.StoreRepository;
import kz.genvibe.media_management.service.internal.AuthService;
import kz.genvibe.media_management.service.internal.StoreService;
import kz.genvibe.media_management.service.internal.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {

    private final UserService userService;
    private final AuthService authService;
    private final StoreRepository storeRepository;
    private final JingleScheduleRepository jingleScheduleRepository;
    private final AppProps appProps;

    @Override
    @Transactional(readOnly = true)
    public List<Store> getAllStores(AppUser appUser) {
        return storeRepository.findAllByOrganization(appUser.getOrganization());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Store> getAllStoresByAppUserAndIdList(AppUser appUser, List<Long> idList) {
        return storeRepository.findStoresByIdIn(idList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActiveStoreDto> getAllActiveStores(AppUser appUser) {
        return storeRepository.findStoresByActiveIsTrueAndOrganization(appUser.getOrganization());
    }

    @Override
    @Transactional(readOnly = true)
    public Store getStoreById(long id) {
        return storeRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public void addStore(AppUser appUser, StoreCreateDto dto) {
        var locationEmail = dto.email();
        var organization = appUser.getOrganization();
        var storeUser = userService.createStoreUser(locationEmail, organization);

        var store = Store.builder()
            .name(dto.name())
            .location(dto.location())
            .email(dto.email())
            .organization(appUser.getOrganization())
            .storeUser(storeUser)
            .build();

        storeRepository.save(store);
        log.info("Added store: {} for user: {}", dto.name(), appUser.getEmail());
    }

    @Override
    @Transactional
    public void activateStore(long id, AppUser appUser) {
        var store = storeRepository.findStoreByIdAndOrganization(id, appUser.getOrganization())
            .orElseThrow(() -> new EntityNotFoundException("Store not found"));

        var uuid = UUID.randomUUID();

        store.setActive(true);
        store.setMusicLinkUuid(uuid);
        store.setMusicLink(generateMusicAccessLink(id, uuid));
        store.setJingleSchedule(jingleScheduleRepository.save(new JingleSchedule(store)));

        authService.sendStoreEmailVerification(store);

        log.info("Activated store: {} with id: {}", store.getName(), id);
    }

    @Override
    @Transactional
    public String regenerateMusicAccessLink(long id, AppUser appUser) {
        var store = storeRepository.findStoreByIdAndOrganization(id, appUser.getOrganization())
            .orElseThrow(() -> new EntityNotFoundException("Store not found"));

        var newUuid = UUID.randomUUID();
        String newLink = generateMusicAccessLink(id, newUuid);

        store.setMusicLinkUuid(newUuid);
        store.setMusicLink(newLink);

        log.info("Regenerated music access link for store: {} with id: {}", store.getName(), id);

        return newLink;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verifyStore(long id, UUID uuid, AppUser appUser) {
        return storeRepository.findStoreByIdAndOrganization(id, appUser.getOrganization())
            .map(store -> store.isActive() && uuid.equals(store.getMusicLinkUuid()))
            .orElse(false);
    }

    private String generateMusicAccessLink(long id, UUID uuid) {
        return appProps.getBaseUrl() + "/stores/" + id + "/" + uuid;
    }

}
