package maoi.platforme.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CreatePartieRequest {
    private String title;
    private Integer orderIndex;
    private List<Long> chapterIds;
}
