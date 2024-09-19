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
    private String duration;
    private String note;
    private List<TrainingChaptersDTO> trainingChaptersDTO;
    private String imageCover;
    private Date createdAt;
    private Date updatedAt;



}
