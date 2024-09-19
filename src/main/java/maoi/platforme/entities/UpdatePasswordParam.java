package maoi.platforme.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordParam {
    private String email;
    private String code;
    private String newPassword;
}
