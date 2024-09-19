package maoi.platforme.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "serviceContents")
@NoArgsConstructor
@AllArgsConstructor
public class ServicesContents {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContent;
    private String content;
    @ManyToOne
    private Services services;
}
