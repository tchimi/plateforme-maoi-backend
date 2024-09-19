package maoi.platforme.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Events {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IdEvent;
    private String title;
    private String slug;
    private String description;
    private String imageCover;
    private String date;
    private Date createdAt;
    private Date updatedAt;
}