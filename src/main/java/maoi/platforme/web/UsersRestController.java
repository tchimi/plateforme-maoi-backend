package maoi.platforme.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maoi.platforme.dtos.AuthenticationDto;
import maoi.platforme.dtos.ListUsersDTO;
import maoi.platforme.dtos.UsersDTO;
import maoi.platforme.entities.UpdatePasswordParam;
import maoi.platforme.exception.*;
import maoi.platforme.services.UsersService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@AllArgsConstructor
@Slf4j
public class UsersRestController {
    private UsersService usersService;
    private AuthenticationManager authenticationManager;

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(path = "/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ListUsersDTO listUsers(@RequestParam(name = "page", defaultValue = "0") int page,
                                  @RequestParam(name = "size", defaultValue = "10") int size) throws UserNotFoundException, UsersAdminException {
        ListUsersDTO listUsersDTO = usersService.getUsers(page, size);
        return listUsersDTO;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(path = "v1/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UsersDTO getUsersDTO(@PathVariable(name = "userId") Long idUsers) throws UserNotFoundException {
        return usersService.getUser(idUsers);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(path = "v1/users/getUserByEmail/", produces = MediaType.APPLICATION_JSON_VALUE)
    public UsersDTO getUserByEmail(@RequestParam (name = "userEmail") String userEmail) throws UserNotFoundException {
        return usersService.getUsersByEmail(userEmail);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(path = "v1/users/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ListUsersDTO searchUsersByName(@RequestParam(name = "searchTerm") String searchTerm,
                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                          @RequestParam(name = "size", defaultValue = "10") int size) throws UserNotFoundException {
        return usersService.searchUsers(searchTerm, page, size);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(path = "v1/users/save")
    public UsersDTO saveUsers(@ModelAttribute UsersDTO usersDTO,
                              @RequestParam(name = "file") MultipartFile file) throws UserMailInvalidException, UserMailUsedException, StorageException {
        return usersService.saveUsers(usersDTO, file);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(path = "v1/users/update/{usersId}")
    public UsersDTO updateUsers(@PathVariable(name = "usersId") Long usersId,
                                @ModelAttribute UsersDTO usersDTO,
                                @RequestParam(name = "file") MultipartFile file) throws UserMailInvalidException, UserNotFoundException, StorageException {
        return this.usersService.updateUsers(usersId, usersDTO, file);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(path = "v1/activeUsersAccount")
    public UsersDTO activeUsersAccount(@RequestBody Map<String, String> activationProperties) throws UserNotFoundException, CodeValidationException, ValidationCodeNotExist {
        return this.usersService.activeUsersAccount(activationProperties);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(path = "v1/authentication")
    public Map<String, String> authentication(@RequestBody AuthenticationDto authenticationDto) {
        return this.usersService.authentication(authenticationDto, this.authenticationManager);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(path = "v1/logout")
    public void logout() {
        this.usersService.logout();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(path = "v1/updatePassword")
    public void updatePassWord(@RequestParam String email) {
        this.usersService.updatePassword(email);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(path = "v1/newPassword")
    public void newPassWord(@RequestBody UpdatePasswordParam parameters) throws ValidationCodeNotExist {
        this.usersService.newPassword(parameters);
    }


    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(path = "v1/refresh-token")
    public @ResponseBody Map<String, String> refreshToken(@RequestBody Map<String, String> refreshTokenParameters) {
        return this.usersService.refreshToken(refreshTokenParameters);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "v1/users/delete/{usersId}")
    public void deleteUsers(@PathVariable(name = "usersId") Long idUsers) throws UserNotFoundException, IOException {
        this.usersService.deleteUsers(idUsers);
    }

    @GetMapping(path = "v1/users/ProfileImage")
    public Resource getUserProfileImage(@RequestParam(name = "fileName") String fileName) throws  StorageFileNotFoundException {
        Resource resource = this.usersService.getProfileImage(fileName);
        return resource;
    }

}
