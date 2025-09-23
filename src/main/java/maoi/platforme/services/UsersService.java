package maoi.platforme.services;

import maoi.platforme.dtos.AuthenticationDto;
import maoi.platforme.dtos.ListUsersDTO;
import maoi.platforme.dtos.UsersDTO;
import maoi.platforme.entities.UpdatePasswordParam;
import maoi.platforme.exception.*;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface UsersService {

    UsersDTO saveUsers(UsersDTO usersDTO, MultipartFile file) throws UserMailUsedException, UserMailInvalidException, StorageException;

    UsersDTO updateUsers(Long usersId, UsersDTO usersDTO, MultipartFile file) throws UserMailInvalidException, UserNotFoundException, StorageException;

    void deleteUsers(Long idUsers) throws UserNotFoundException, IOException;

    ListUsersDTO getUsers(int page, int size) throws UserNotFoundException, UsersAdminException;

    UsersDTO getUsersByEmail(String email) throws UserNotFoundException;

    UsersDTO getUser(Long idUsers) throws UserNotFoundException;

    ListUsersDTO searchUsers(String searchTerm, int page, int size) throws UserNotFoundException;

    Resource getProfileImage(String filename) throws StorageFileNotFoundException;

    Map<String, String> authentication(AuthenticationDto authenticationDto, AuthenticationManager authenticationManager);

    void logout();

    UsersDTO activeUsersAccount(Map<String, String> activationProperties) throws UserNotFoundException, CodeValidationException, ValidationCodeNotExist;

    void updatePassword(String email);

    void newPassword(UpdatePasswordParam parameters) throws ValidationCodeNotExist;

    Map<String,String> refreshToken(Map<String, String> refreshTokenProperties);
}
