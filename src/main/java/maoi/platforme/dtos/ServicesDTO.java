package maoi.platforme.dtos;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ServicesDTO {
    private Long idService;
    private String title;
    private String slug;
    private String imageCover;
    private Date createdAt;
    private Date updatedAt;
    private List<ServicesContentsDTO> servicesContentsDTO;
}
