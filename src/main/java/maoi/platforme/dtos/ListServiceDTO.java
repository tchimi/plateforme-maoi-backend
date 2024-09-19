package maoi.platforme.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ListServiceDTO {

    private List<ServicesDTO> listServicesDTO;
    private int currentPage;
    private int pageSize;
    private int totalPages;
}
