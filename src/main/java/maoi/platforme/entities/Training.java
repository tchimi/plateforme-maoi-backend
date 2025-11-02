package maoi.platforme.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "training")
@AllArgsConstructor
@NoArgsConstructor
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String slug;
    private String description;

    @ElementCollection
    @CollectionTable(name = "training_objectives", joinColumns = @JoinColumn(name = "training_id"))
    @Column(name = "objective")
    private List<String> objectifs;

    private String prerequis;
    private boolean certificat;

    private String duration;
    private Double price;
    private Double note;
    private String imageCover;

    private Date createdAt;
    private Date updatedAt;

    // Relation avec les parties
    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TrainingPartie> parties;

}
