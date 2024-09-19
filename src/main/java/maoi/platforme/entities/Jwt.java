package maoi.platforme.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.engine.internal.Cascade;

import java.util.Date;

@Entity
@Table(name = "jwt")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Jwt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bearerToken;
    private boolean bearerTokenDisabled;
    private boolean bearerTokenExpire;
    @OneToOne(cascade= CascadeType.ALL, fetch = FetchType.EAGER)
    private RefreshToken refreshToken;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    private Users users;
}
