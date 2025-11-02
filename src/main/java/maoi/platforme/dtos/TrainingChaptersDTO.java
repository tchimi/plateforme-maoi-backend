package maoi.platforme.dtos;

import lombok.Data;

import java.util.List;

@Data
public class TrainingChaptersDTO {
    private Long id;
    private String title;
    private String description;
    private String descriptionHtml;
    private String video; // chemin ou URL
    private String duration;
    private int chapterOrder;
    private List<TrainingAssetDTO> assets;
}
