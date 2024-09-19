package maoi.platforme.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ListTestimonyDTO {
    private List<TestimonyDTO> listTestimonyDTO;
    private int currentPage;
    private int pageSize;
    private int totalPages;
}
