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
    private String duration;
    private String note;
    private String imageCover;
    private Date createdAt;
    private Date updatedAt;

    @OneToMany(mappedBy = "idChapters",fetch = FetchType.LAZY)
    private List<TrainingChapters> trainingChaptersList;

}
