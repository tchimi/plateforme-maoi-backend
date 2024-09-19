package maoi.platforme.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "testimony")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Testimony {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String slug;
    private String message;
    private String profession;
    private String userProfile;
    private Date createdAt;
    private Date updatedAt;
}
