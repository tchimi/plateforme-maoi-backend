package maoi.platforme.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@Table(name = "validation")
@NoArgsConstructor
@AllArgsConstructor
public class Validation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant createdAt;
    private Instant activation;
    private Instant expire;
    private String code;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH})
    private Users users;

}
