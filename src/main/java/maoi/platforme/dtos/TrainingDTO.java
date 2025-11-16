package maoi.platforme.dtos;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TrainingDTO {
    private Long id;
    private String title;
    private String slug;
    private String description;
    private List<String> objectifs;
    private String prerequis;
    private boolean certificat;

    private String duration;
    private Double price;
    private Double note;

    private String imageCover;
    private Date createdAt;
    private Date updatedAt;

}
