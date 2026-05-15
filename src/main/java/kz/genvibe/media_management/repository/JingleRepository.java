package kz.genvibe.media_management.repository;

import jakarta.transaction.Transactional;
import kz.genvibe.media_management.model.entity.Jingle;
import kz.genvibe.media_management.model.entity.Organization;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JingleRepository extends JpaRepository<Jingle, Long> {
    @EntityGraph(attributePaths = "stores")
    List<Jingle> findJinglesByOrganization(Organization organization);

    @EntityGraph(attributePaths = "stores")
    List<Jingle> findJinglesByOrganizationAndRequestedToPauseIsTrue(Organization organization);

    Optional<Jingle> findJingleByIdAndOrganization(Long id, Organization organization);

    long countAllByOrganization(Organization organization);

    @Modifying
    @Transactional
    @Query("DELETE FROM Jingle j WHERE j.id = :id AND j.organization = :org")
    void deleteByIdAndOrg(@Param("id") long id, @Param("org") Organization org);
}
