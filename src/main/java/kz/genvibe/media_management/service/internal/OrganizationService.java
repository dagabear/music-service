package kz.genvibe.media_management.service.internal;

import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.Organization;
import kz.genvibe.media_management.model.enums.MusicAtmosphere;
import kz.genvibe.media_management.model.enums.MusicMood;

import java.util.List;

public interface OrganizationService {
    Organization createOrganizationFromOnboarding(String email);
    void saveMusicTypes(AppUser appUser, MusicAtmosphere atmosphere, MusicMood mood);
    boolean existsByCompanyName(String companyName);
}
