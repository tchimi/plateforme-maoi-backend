package maoi.platforme.repositories;

import maoi.platforme.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(@Param("email") String userEmail);

    @Query("select u from Users u where u.name like:searchTerm")
    Page<Users> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);

}
