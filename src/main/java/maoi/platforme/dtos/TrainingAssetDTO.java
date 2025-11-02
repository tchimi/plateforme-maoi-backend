package maoi.platforme.dtos;

import lombok.Data;

@Data
public class TrainingAssetDTO {
    private Long id;
    private String name;
    private String type;
    private String url;
    private Long size;
}
