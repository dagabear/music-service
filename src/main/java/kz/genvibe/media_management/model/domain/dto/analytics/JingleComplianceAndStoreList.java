package kz.genvibe.media_management.model.domain.dto.analytics;

import kz.genvibe.media_management.model.enums.JingleCategory;

import java.util.List;

public record JingleComplianceAndStoreList(
    List<JingleComplianceAndStore> jingleComplianceAndStores
) {

    public record JingleComplianceAndStore(
        String storeName,
        JingleCategory jingleCategory,
        String targetTime,
        String actualTime,
        String status
    ) {}

}
