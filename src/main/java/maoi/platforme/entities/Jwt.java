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
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "refresh_token_id")
    private RefreshToken refreshToken;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id_users", nullable = false)
    private Users users;
}
