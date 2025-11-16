package maoi.platforme.configuration;

import jakarta.annotation.PostConstruct;
import maoi.platforme.entities.Role;
import maoi.platforme.enums.TypeDeRole;
import maoi.platforme.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void initRoles() {

        if (!roleRepository.existsByLibelle(TypeDeRole.UTILISATEUR)) {
            Role userRole = new Role();
            userRole.setLibelle(TypeDeRole.UTILISATEUR);
            roleRepository.save(userRole);
        }

        if (!roleRepository.existsByLibelle(TypeDeRole.ADMINISTRATEUR)) {
            Role adminRole = new Role();
            adminRole.setLibelle(TypeDeRole.ADMINISTRATEUR);
            roleRepository.save(adminRole);
        }

        if (!roleRepository.existsByLibelle(TypeDeRole.FORMATEUR)) {
            Role formateurRole = new Role();
            formateurRole.setLibelle(TypeDeRole.FORMATEUR);
            roleRepository.save(formateurRole);
        }
    }

}
