package maoi.platforme.services;

import lombok.AllArgsConstructor;
import maoi.platforme.entities.Validation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private JavaMailSender javaMailSender;
    //@Value("{email.Sender}")
    //private String emailSender = "mike";
    @Override
    public void sendEmail(Validation validation) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("mike");
        String userMail = validation.getUsers().getEmail();
        mailMessage.setTo(userMail);
        mailMessage.setSubject("votre code de validation");
        String validationCode = validation.getCode();
        String text = String.format("Bienvenu sur la plateForme Maoi <br/> votre code d'activation est le suivant %s <br/>",validationCode);
        mailMessage.setText(text);
        javaMailSender.send(mailMessage);
    }
}
