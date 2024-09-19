package maoi.platforme.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class TestimonyDTO {
    private Long id;
    private String userName;
    private String slug;
    private String message;
    private String profession;
    private String userProfile;
    private Date createdAt;
    private Date updatedAt;
}
