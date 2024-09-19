package maoi.platforme.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "services")
@AllArgsConstructor
@NoArgsConstructor
public class Services {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idService;
    private String title;
    private String slug;
    private String imageCover;
    private Date creatAt;
    private Date updatedAt;

    @OneToMany(mappedBy = "idContent",fetch = FetchType.LAZY)
    private List<ServicesContents> contents;
}
