package maoi.platforme.securite;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import maoi.platforme.entities.Jwt;
import maoi.platforme.entities.Users;
import maoi.platforme.repositories.UsersRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JwtFilter extends OncePerRequestFilter {

    //private UsersServiceImpl usersService;
    private JwtService jwtService;
    private UsersRepository usersRepository;

    public JwtFilter(JwtService jwtService, UsersRepository usersRepository) {
        this.jwtService = jwtService;
        this.usersRepository = usersRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userName;
        String bearerToken;
        boolean isBearerTokenExpire;
        Jwt jwtInDB;

        String authorization = request.getHeader("Authorization");
        if(authorization != null && authorization.startsWith("Bearer ")){
            bearerToken = authorization.substring(7);
            jwtInDB = this.jwtService.getTokenByBearerToken(bearerToken);
            isBearerTokenExpire = this.jwtService.tokenExpired(bearerToken);
            if(!isBearerTokenExpire){
                userName = this.jwtService.extractUserName(bearerToken);
                if(jwtInDB.getUsers().getEmail().equals(userName) && SecurityContextHolder.getContext().getAuthentication() == null){
                    Users userDetails = this.usersRepository.findByEmail(userName).orElseThrow();
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
