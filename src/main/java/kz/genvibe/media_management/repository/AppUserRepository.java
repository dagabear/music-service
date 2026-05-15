package kz.genvibe.media_management.repository;

import kz.genvibe.media_management.model.entity.AppUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"organization.stores", "organization.jingles"})
    Optional<AppUser> findWithDetailsByEmail(String email);
}
