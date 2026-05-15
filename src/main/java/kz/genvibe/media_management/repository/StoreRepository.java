package kz.genvibe.media_management.repository;

import kz.genvibe.media_management.model.domain.dto.store.ActiveStoreDto;
import kz.genvibe.media_management.model.entity.Organization;
import kz.genvibe.media_management.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findAllByOrganization(Organization organization);
    List<Store> findStoresByOrganizationAndNameIn(Organization organization, List<String> names);
    Optional<Store> findStoreByIdAndOrganization(Long id, Organization organization);

    List<ActiveStoreDto> findStoresByActiveIsTrueAndOrganization(Organization organization);

    List<Store> findStoresByIdIn(List<Long> idList);
}
