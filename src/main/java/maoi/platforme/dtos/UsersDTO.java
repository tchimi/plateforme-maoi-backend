package maoi.platforme.dtos;

import lombok.Data;
import maoi.platforme.entities.Role;
import maoi.platforme.enums.LanguageLocal;
import maoi.platforme.enums.TypeSexe;

import java.util.Date;

@Data
public class UsersDTO {
    private Long idUsers;
    private String email;
    private String profession;
    private boolean isAdmin;
    private String name;
    private String phoneNumber;
    private String dateOfBirth;
    private TypeSexe sexe;
    private String password;
    private String profileImage;
    private LanguageLocal locale;
    private String token;
    private Date createdAt;
    private Date updatedAt;
    private boolean actif = false;
    private Role role;
}
