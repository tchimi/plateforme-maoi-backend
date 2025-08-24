package maoi.platforme.mappers;

import maoi.platforme.dtos.JwtDTO;
import maoi.platforme.dtos.UsersDTO;
import maoi.platforme.entities.Jwt;
import maoi.platforme.entities.Users;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class JwtMapperImpl {

    public JwtDTO fromJwt (Jwt jwt) {
        JwtDTO jwtDTO = new JwtDTO();
        BeanUtils.copyProperties(jwt, jwtDTO);
        return jwtDTO;
    }
}
