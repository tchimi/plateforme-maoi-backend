package maoi.platforme.dtos;

import lombok.Data;
import maoi.platforme.entities.RefreshToken;
import maoi.platforme.entities.Users;

@Data
public class JwtDTO {
    private String bearerToken;
    private boolean bearerTokenDisabled;
    private boolean bearerTokenExpire;
    private RefreshToken refreshToken;
    private Users users;
}
