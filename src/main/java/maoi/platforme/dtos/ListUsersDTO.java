package maoi.platforme.dtos;

import lombok.Data;

import java.util.List;
@Data
public class ListUsersDTO {

    private List<UsersDTO> listUsersDTO;
    private int currentPage;
    private int pageSize;
    private int totalPages;
}
