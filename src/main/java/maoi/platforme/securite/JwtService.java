package maoi.platforme.securite;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maoi.platforme.dtos.JwtDTO;
import maoi.platforme.entities.Jwt;
import maoi.platforme.entities.RefreshToken;
import maoi.platforme.entities.Users;
import maoi.platforme.mappers.JwtMapperImpl;
import maoi.platforme.repositories.JwtRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.Instant;
import java.util.*;

import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@AllArgsConstructor
@Service
public class JwtService {
    public static final String BEARER = "bearer";

    private final String ENCRYPTION_KEYS = "adc6cc6148069b0c467e190f213c07b784c2883e3310d2200d8fc85cf8477d91";
    private JwtRepository jwtRepository;
    private JwtMapperImpl jwtMapper;

    public Map<String, String> generate(Users userDetails){
        this.disableBearerToken(userDetails);
        Map<String, String> jwtMap = new HashMap<>(this.generateJwt(userDetails));
        RefreshToken refreshToken = this.generateRefreshToken();
        Jwt jwt = Jwt.builder().bearerToken(jwtMap.get(BEARER)).bearerTokenDisabled(false).bearerTokenExpire(false).users(userDetails).refreshToken(refreshToken).build();
        this.jwtRepository.save(jwt);
        jwtMap.put("refresh", refreshToken.getRefreshToken());
        return jwtMap;
    }


    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshTokenExpire(Instant.now().plusMillis(60*60*1000))
                .refreshTokenCreation(Instant.now())
                .expire(false)
                .refreshToken(UUID.randomUUID().toString())
                .build();
        return refreshToken;
    }

    public String extractUserName(String bearerToken) {
        //Jwt jwt = this.getTokenByBearerToken(bearerToken);
        String userName = this.getClaim(bearerToken, Claims::getSubject);
        return userName;
        //return jwt.getUsers().getEmail();
    }

    public boolean tokenExpired(String bearerToken) {
        Date expirationTokenDate = this.getClaim(bearerToken, Claims::getExpiration);
        boolean bearerTokenIsExpire = false;
        if(expirationTokenDate.before(new Date())){
            bearerTokenIsExpire = true;
        }
        return bearerTokenIsExpire;
    }


    public Jwt getTokenByBearerToken(String bearerToken ){
        return this.jwtRepository.findByBearerToken(bearerToken).orElseThrow(()-> new RuntimeException("ce token n'existe pas"));
    }

    @Transactional(readOnly = true)
    public JwtDTO getTokenDTOByBearerToken(String bearerToken ){
         Jwt jwt = this.jwtRepository.findByBearerToken(bearerToken).orElseThrow(()-> new RuntimeException("ce token n'existe pas"));
        JwtDTO jwtDTO  = jwtMapper.fromJwt(jwt);
        return jwtDTO;
    }

    public Jwt getJwtByRefreshToken(String refreshToken ){
        return this.jwtRepository.findByRefreshToken(refreshToken).orElseThrow(()-> new RuntimeException("ce refresh n'existe pas"));
    }

    public void logout(){
        Users loginUser = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = this.jwtRepository.findUsersValidBearerToken(loginUser.getEmail(), false, false).orElseThrow(()-> new RuntimeException("TOKEN invalide"));
        jwt.setBearerTokenDisabled(true);
        jwt.setBearerTokenExpire(true);
    }

    //@Scheduled(cron = "* */1 * * * *")
    @Scheduled(cron = "@daily")
    public void removeUseLessJwt(){
        log.info("suppression des token non utiliser {}::",Instant.now());
        this.jwtRepository.deleteAllByBearerTokenExpireAndBearerTokenDisabled(true,true);
    }

    private <T> T getClaim(String bearerToken, Function<Claims, T> function) {
        Claims claims = getAllClaims(bearerToken);
        return function.apply(claims);
    }

    private Claims getAllClaims(String bearerToken) {
        return Jwts.parser()
                .setSigningKey(this.getKeys())
                .build()
                .parseClaimsJws(bearerToken)
                .getBody();
    }

    private Map<String, String> generateJwt(Users userDetails){
        Long currentTime = System.currentTimeMillis();
        Long expirationTime = currentTime + 30*60*1000;

        Map<String, Object> cleams = Map.of(
            "nom", userDetails.getName(),
            "email",  userDetails.getEmail(),
             Claims.EXPIRATION, new Date(expirationTime),
             Claims.SUBJECT, userDetails.getEmail()
        );
        String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(userDetails.getUsername())
                .setClaims(cleams)
                .signWith(getKeys(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of(BEARER, bearer);
    }

    private Key getKeys() {
        byte[] decode = Decoders.BASE64.decode(ENCRYPTION_KEYS);
        return Keys.hmacShaKeyFor(decode);
    }

    private void disableBearerToken(Users users){
        List<Jwt> jwtList = this.jwtRepository.findByUsers(users.getEmail()).peek(
                jwt -> {
                    jwt.setBearerTokenExpire(true);
                    jwt.setBearerTokenDisabled(true);
                }
        ).collect(Collectors.toList());
        this.jwtRepository.saveAll(jwtList);
    }


}
