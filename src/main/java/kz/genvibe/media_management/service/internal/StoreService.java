package kz.genvibe.media_management.service.internal;

import kz.genvibe.media_management.model.domain.dto.store.ActiveStoreDto;
import kz.genvibe.media_management.model.domain.dto.store.StoreCreateDto;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.Store;

import java.util.List;
import java.util.UUID;

public interface StoreService {
    // Read methods
    List<Store> getAllStores(AppUser appUser);
    List<Store> getAllStoresByAppUserAndIdList(AppUser appUser, List<Long> idList);
    List<ActiveStoreDto> getAllActiveStores(AppUser appUser);
    Store getStoreById(long id);

    // Modification methods
    void addStore(AppUser appUser, StoreCreateDto dto);
    void activateStore(long id, AppUser appUser);
    String regenerateMusicAccessLink(long id, AppUser appUser);

    // Verification methods
    boolean verifyStore(long id, UUID uuid, AppUser appUser);
}
