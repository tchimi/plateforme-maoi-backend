package maoi.platforme.repositories;

import maoi.platforme.entities.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ServicesRepository extends JpaRepository<Services, Long> {
    Optional<Services> findBySlug(@Param("slug") String slug);
}
