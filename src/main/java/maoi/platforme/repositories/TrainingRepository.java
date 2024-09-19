package maoi.platforme.repositories;


import maoi.platforme.entities.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    Optional<Training> findBySlug(@Param("slug") String slug);
}
