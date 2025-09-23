package maoi.platforme.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maoi.platforme.dtos.AuthenticationDto;
import maoi.platforme.dtos.ListUsersDTO;
import maoi.platforme.dtos.UsersDTO;
import maoi.platforme.entities.*;
import maoi.platforme.enums.TypeDeRole;
import maoi.platforme.exception.*;
import maoi.platforme.mappers.UsersMapperImpl;
import maoi.platforme.repositories.RoleRepository;
import maoi.platforme.repositories.UsersRepository;
import maoi.platforme.securite.JwtService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
// dependence qui permet d'ajout les getter et les setter de tout les variable declacrer et aussi un constructer de avec arguments
@Slf4j // dependence utiliser pour le logger les methode

public class UsersServiceImpl implements UsersService, UserDetailsService {
    private UsersRepository usersRepository;
    private UsersMapperImpl usersMapper;
    private UploadFileService uploadFileService;
    private JwtService jwtService;
    private ValidationService validationService;
    //private AuthenticationManager authenticationManager;
    private final Path rootLocation = Paths.get("upload/");
    private final String storageDir = "users/profileImage";
    private RoleRepository roleRepository;

    //Logger LOG = LoggerFactory.getLogger(UsersServiceImpl.class);
    private BCryptPasswordEncoder passwordEncoder ;

    @Override
    public UsersDTO saveUsers(UsersDTO usersDTO, MultipartFile file) throws UserMailUsedException, UserMailInvalidException, StorageException {

        if (usersDTO.getEmail() == null) {
            throw new UserMailInvalidException("email not found");
        }

        if (!usersDTO.getEmail().contains("@")) {
            throw new UserMailInvalidException("Votre mail invalide");
        }
        if (!usersDTO.getEmail().contains(".")) {
            throw new UserMailInvalidException("Votre mail invalide");
        }
        Optional userOptional = this.usersRepository.findByEmail(usersDTO.getEmail());
        if (userOptional.isPresent()) {
            throw new UserMailUsedException("Votre mail est déjà utilisé");
        }
        String fileName = uploadFileService.uploadFile(file, storageDir, rootLocation);

        usersDTO.setProfileImage(fileName);
        usersDTO.setCreatedAt(new Date());
        usersDTO.setUpdatedAt(usersDTO.getCreatedAt());
        String mdpCrypte = this.passwordEncoder.encode(usersDTO.getPassword());
        usersDTO.setPassword(mdpCrypte);
        Role userRole = findOrCreateRole(usersDTO.isAdmin());
        try {
            Users users = usersMapper.fromUserDTO(usersDTO);
            users.setRole(userRole);
            Users saveUsers = usersRepository.save(users);
            this.validationService.save(saveUsers);
            return usersMapper.fromUsers(saveUsers);

        } catch (Exception e) {
            log.error("an error has occurred while executing the method saveUsers() " + usersDTO.getName(), e);
            return null;
        }
    }

    private Role findOrCreateRole(boolean isAdmin) {
        TypeDeRole roleType = isAdmin ? TypeDeRole.ADMINISTRATEUR : TypeDeRole.UTILISATEUR;

        Optional<Role> existingRole = roleRepository.findByLibelle(roleType);

        if (existingRole.isPresent()) {
            return existingRole.get();
        } else {
            Role newRole = new Role();
            newRole.setLibelle(roleType);
            return roleRepository.save(newRole);
        }
    }

    @Override
    public UsersDTO updateUsers(Long usersId, UsersDTO usersDTO, MultipartFile file) throws UserMailInvalidException, UserNotFoundException, StorageException {

        UsersDTO oldUserDTO = getUser(usersId);
        if (oldUserDTO == null) {
            throw new UserNotFoundException("Utilisateur non trouvé");
        }

        usersDTO.setEmail(oldUserDTO.getEmail());
        usersDTO.setPassword(oldUserDTO.getPassword());

        if (file != null) {
            String fileName = uploadFileService.uploadFile(file, storageDir, rootLocation);
            usersDTO.setProfileImage(fileName);
        }

        usersDTO.setIdUsers(usersId);
        usersDTO.setUpdatedAt(new Date());
        usersDTO.setRole(getUserRole(usersId));

        try {
            Users users = usersMapper.fromUserDTO(usersDTO);
            Users updateUsers = usersRepository.save(users);
            return usersMapper.fromUsers(updateUsers);
        } catch (Exception e) {
            log.error("an error has occurred while executing the method updateUsers() {}", usersDTO.getName(), e);
            return null;
        }
    }

    @Override
    public void deleteUsers(Long idUsers) throws UserNotFoundException, IOException {
        Users user = usersRepository.findById(idUsers).orElseThrow(() -> new UserNotFoundException("user not found"));
        if (user != null) {
            usersRepository.deleteById(idUsers);
            String filename = user.getProfileImage();
            uploadFileService.deleteFile(filename, storageDir, this.rootLocation);
        }
    }

    @Override
    public ListUsersDTO getUsers(int page, int size) throws UserNotFoundException, UsersAdminException {
        jwtService.actionAdminOnly();
        Sort.Direction direction = Sort.Direction.fromString("ASC");
        String sortBy = "updatedAt";
        Page<Users> listUsers = usersRepository.findAll(PageRequest.of(page, size, Sort.by(direction,sortBy)));
        if (listUsers.isEmpty()) {
            throw new UserNotFoundException("list user not found");
        }
        Page<UsersDTO> pageUsersDTO = listUsers.map(user -> usersMapper.fromUsers(user));
        return usersMapper.fromPageUsersDTO(pageUsersDTO);
    }

    @Override
    public UsersDTO getUsersByEmail(String email) throws UserNotFoundException {
        Users users = usersRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("user not found"));
        return usersMapper.fromUsers(users);
    }

    @Override
    public UsersDTO getUser(Long idUsers) throws UserNotFoundException {
        Users user = usersRepository.findById(idUsers).orElseThrow(() -> new UserNotFoundException("user not found"));
        return usersMapper.fromUsers(user);
    }

    @Override
    public ListUsersDTO searchUsers(String searchTerm, int page, int size) throws UserNotFoundException {
        Page<Users> listUserSearch = usersRepository.searchUsers("%" + searchTerm + "%", PageRequest.of(page, size));
        if (listUserSearch.isEmpty()) {
            throw new UserNotFoundException("list user not found");
        }
        Page<UsersDTO> pageUsersDTOSearch = listUserSearch.map(user -> usersMapper.fromUsers(user));
        return usersMapper.fromPageUsersDTO(pageUsersDTOSearch);
    }

    @Override
    public Resource getProfileImage(String fileName) throws StorageFileNotFoundException {
        return uploadFileService.loadAsResource(fileName, storageDir, this.rootLocation);
    }


    @Override
    public Map<String, String> authentication(AuthenticationDto authenticationDto, AuthenticationManager authenticationManager) {
        final Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationDto.getUserName(), authenticationDto.getPassword())
        );
        if(authenticate.isAuthenticated()){
            Users userDetails = loadUserByUsername(authenticationDto.getUserName());
            return this.jwtService.generate(userDetails);
        }
        return null;
    }

    @Override
    public void logout() {
        this.jwtService.logout();
    }

    @Override
    public UsersDTO activeUsersAccount(Map<String, String> activationProperties) throws UserNotFoundException, CodeValidationException, ValidationCodeNotExist {
        Validation validation =  validationService.getValidationByCode(activationProperties.get("code"));
        if (validation != null) {
            if (Instant.now().isAfter(validation.getExpire())) {
                throw new CodeValidationException("votre code de validation a expirer");
            }
            Users usersToActive = usersRepository.findById(validation.getUsers().getIdUsers()).orElseThrow(() -> new UserNotFoundException("user not found"));
            usersToActive.setActif(true);
            usersRepository.save(usersToActive);
        }
        return null;
    }

    @Override
    public Users loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return this.usersRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("user not found"));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updatePassword(String email) {
        Users users = this.loadUserByUsername(email);
        this.validationService.save(users);
    }

    @Override
    public void newPassword(UpdatePasswordParam parameters) throws ValidationCodeNotExist {
        Users users = this.loadUserByUsername(parameters.getEmail());
        Validation validation = this.validationService.getValidationByCode(parameters.getCode());
        if(validation.getUsers().getEmail().equals(users.getEmail())){
            String mdpCrypte = this.passwordEncoder.encode(parameters.getNewPassword());
            users.setPassword(mdpCrypte);
            this.usersRepository.save(users);
        }

    }

    @Override
    public Map<String,String> refreshToken(Map<String, String> refreshTokenProperties) {
        Jwt jwt = this.jwtService.getJwtByRefreshToken(refreshTokenProperties.get("refreshToken"));
        RefreshToken refreshToken = jwt.getRefreshToken();
        if(refreshToken.isExpire() || refreshToken.getRefreshTokenExpire().isBefore(Instant.now())){
            throw new RuntimeException("refresh token is expire");
        }
        Map<String, String> tokens = this.jwtService.generate(jwt.getUsers());
        return tokens;
    }

    private Role getUserRole(Long idUsers) throws UserNotFoundException{
        if (idUsers == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        Users users = usersRepository.findById(idUsers).orElseThrow(() -> new UserNotFoundException("user not found"));
        return users.getRole();
    }
}
