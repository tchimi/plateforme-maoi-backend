package maoi.platforme.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ListEventsDTO {
    private List<EventsDTO> listEventsDTO;
    private int currentPage;
    private int pageSize;
    private int totalPages;
}
