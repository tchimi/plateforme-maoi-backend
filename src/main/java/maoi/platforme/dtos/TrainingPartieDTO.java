package maoi.platforme.dtos;

import lombok.Data;

import java.util.List;

@Data
public class TrainingPartieDTO {
    private Long id;
    private String title;
    private List<TrainingChaptersDTO> chapters;
}
