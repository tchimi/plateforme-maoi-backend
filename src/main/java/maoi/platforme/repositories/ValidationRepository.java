package maoi.platforme.repositories;

import maoi.platforme.entities.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;

public interface ValidationRepository extends JpaRepository<Validation, Long> {
    Optional<Validation> findByCode(String code);

    void deleteAllByExpireBefore(Instant now);
}
