package maoi.platforme.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "trainingChapters")
@AllArgsConstructor
@NoArgsConstructor
public class TrainingChapters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idChapters;

    private String title;
    private String description;
    private String descriptionHtml;
    private String video; // Stocker l’URL ou le chemin du fichier vidéo
    private String duration;
    private int chapterOrder;

    //Chaque chapitre appartient à une partie
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partie_id")
    private TrainingPartie partie;

    //Liste d’assets (images, pdf, etc.)
    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TrainingAsset> assets;
}
