package maoi.platforme.services;

import maoi.platforme.entities.Users;
import maoi.platforme.entities.Validation;
import maoi.platforme.exception.UserNotFoundException;
import maoi.platforme.exception.ValidationCodeNotExist;

public interface ValidationService {

    void save(Users users);
    Validation getValidationByCode(String code) throws ValidationCodeNotExist;
    void getNewCodeValidation(Long idUser) throws UserNotFoundException;
}
