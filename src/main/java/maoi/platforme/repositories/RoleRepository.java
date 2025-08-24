package maoi.platforme.repositories;

import maoi.platforme.entities.Role;
import maoi.platforme.enums.TypeDeRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByLibelle(TypeDeRole libelle);
    boolean existsByLibelle(TypeDeRole libelle);
}
