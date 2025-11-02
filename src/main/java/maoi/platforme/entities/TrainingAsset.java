package maoi.platforme.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "training_asset")
@AllArgsConstructor
@NoArgsConstructor
public class TrainingAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;     // Nom de l’asset
    private String type;     // Type (PDF, IMAGE, DOC, etc.)
    private String url;      // Lien vers le fichier
    private Long size;       // Taille en octets

    //Chaque asset appartient à un chapitre
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private TrainingChapters chapter;
}
