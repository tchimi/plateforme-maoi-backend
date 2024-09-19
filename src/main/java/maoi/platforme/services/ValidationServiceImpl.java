package maoi.platforme.services;


import lombok.AllArgsConstructor;
import maoi.platforme.entities.Users;
import maoi.platforme.entities.Validation;
import maoi.platforme.exception.ValidationCodeNotExist;
import maoi.platforme.repositories.ValidationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MINUTES;
@Service
@Transactional
@AllArgsConstructor
public class ValidationServiceImpl implements ValidationService {

    private ValidationRepository validationRepository;
    private NotificationService notificationService;
    @Override
    public void save(Users users) {
        Validation confirmation = new Validation();
        confirmation.setUsers(users);
        Instant creationTime = Instant.now();
        confirmation.setCreatedAt(creationTime);
        Instant expiration =  creationTime.plus(10, MINUTES);
        confirmation.setExpire(expiration);
        Random random = new Random();
        int randomInteger = random.nextInt(999999);
        String code = String.format("%06d",randomInteger);
        confirmation.setCode(code);
        this.validationRepository.save(confirmation);
        this.notificationService.sendEmail(confirmation);
    }

    public Validation getValidationByCode(String code) throws ValidationCodeNotExist {
        return  this.validationRepository.findByCode(code).orElseThrow(()-> new ValidationCodeNotExist("Votre code de validation est invalide"));
    }

    @Scheduled(cron = "@daily")
    public void deleteValidationExpire(){
        Instant now = Instant.now();
        this.validationRepository.deleteAllByExpireBefore(now);
    }
}
