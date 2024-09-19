package maoi.platforme.repositories;

import maoi.platforme.entities.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface JwtRepository extends JpaRepository<Jwt, Long> {

    Optional<Jwt> findByBearerToken(String bearerToken);

    @Query("FROM Jwt j WHERE j.bearerTokenExpire = :bearerTokenExpire AND j.bearerTokenDisabled = :bearerTokenDisabled AND j.users.email = :email")
    Optional<Jwt> findUsersValidBearerToken(String email, boolean bearerTokenDisabled, boolean bearerTokenExpire);

    @Query("FROM Jwt j WHERE j.users.email = :email")
    Stream<Jwt> findByUsers(String email);

    @Query("FROM Jwt j WHERE j.refreshToken.refreshToken = :refreshToken")
    Optional<Jwt> findByRefreshToken(String refreshToken);

    void deleteAllByBearerTokenExpireAndBearerTokenDisabled(boolean bearerTokenExpire, boolean bearerTokenDisabled);
}
