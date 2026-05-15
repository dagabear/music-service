package kz.genvibe.media_management.repository;

import kz.genvibe.media_management.model.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    boolean existsByCompanyName(String companyName);
}
