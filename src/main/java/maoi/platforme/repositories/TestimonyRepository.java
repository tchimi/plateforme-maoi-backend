package maoi.platforme.repositories;

import maoi.platforme.entities.Testimony;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TestimonyRepository extends JpaRepository<Testimony, Long> {

    Optional<Testimony> findBySlug(@Param("slug") String slug);

}
