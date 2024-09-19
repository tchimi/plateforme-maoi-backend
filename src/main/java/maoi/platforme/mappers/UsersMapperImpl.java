package maoi.platforme.mappers;

import maoi.platforme.dtos.ListUsersDTO;
import maoi.platforme.dtos.UsersDTO;
import maoi.platforme.entities.Users;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class UsersMapperImpl {

    public UsersDTO fromUsers(Users users) {
        UsersDTO usersDTO = new UsersDTO();
        BeanUtils.copyProperties(users, usersDTO);
        return usersDTO;
    }

    public Users fromUserDTO(UsersDTO usersDTO) {
        Users users = new Users();
        BeanUtils.copyProperties(usersDTO, users);
        return users;
    }

    public ListUsersDTO fromPageUsersDTO(Page<UsersDTO> pageUsersDTO){
        ListUsersDTO listUsersDTO = new ListUsersDTO();
        listUsersDTO.setListUsersDTO(pageUsersDTO.getContent());
        listUsersDTO.setCurrentPage(pageUsersDTO.getNumber());
        listUsersDTO.setPageSize(pageUsersDTO.getSize());
        listUsersDTO.setTotalPages(pageUsersDTO.getTotalPages());
        return listUsersDTO;
    }

}
