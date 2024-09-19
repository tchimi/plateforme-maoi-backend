package maoi.platforme.dtos;


import lombok.Data;

import java.util.Date;

@Data
public class EventsDTO {
    private Long IdEvent;
    private String title;
    private String slug;
    private String description;
    private String imageCover;
    private String date;
    private Date createdAt;
    private Date updatedAt;
}
