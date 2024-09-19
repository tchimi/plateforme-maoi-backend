package maoi.platforme.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "trainingChapters")
@AllArgsConstructor
@NoArgsConstructor
public class TrainingChapters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idChapters;
    private String chapters;
    @ManyToOne
    private Training training;
}
