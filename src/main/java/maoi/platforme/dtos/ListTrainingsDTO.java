package maoi.platforme.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ListTrainingsDTO {

    private List<TrainingDTO> listTrainingDTO;
    private int currentPage;
    private int pageSize;
    private int totalPages;
}
