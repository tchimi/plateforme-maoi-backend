package maoi.platforme.repositories;

import maoi.platforme.entities.Events;
import maoi.platforme.entities.Testimony;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface EventsRepository extends JpaRepository<Events, Long> {

    Optional<Events> findBySlug(@Param("slug") String slug);
}
