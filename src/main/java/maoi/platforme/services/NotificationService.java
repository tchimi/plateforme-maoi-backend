package maoi.platforme.services;

import maoi.platforme.entities.Validation;

public interface NotificationService {
    void sendEmail(Validation validation);
}
