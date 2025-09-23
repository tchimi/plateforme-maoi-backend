package maoi.platforme.web.advice;

import maoi.platforme.dtos.ErrorEntity;
import maoi.platforme.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@ControllerAdvice
public class ApplicationControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UserNotFoundException.class, UserMailInvalidException.class,
            StorageFileNotFoundException.class, StorageException.class,
            TestimonyNotFoundException.class, TestimonySlugInvalideException.class,
            EventNotFoundException.class, EventsSlugInvalidException.class,
            ServicesNotFoundException.class, ServicesSlugInvalidException.class,
            CodeValidationException.class, ValidationCodeNotExist.class,
            TrainingNotFoundException.class,TrainingSlugInvalidException.class,
            BearerTokenNotFoundException.class,
            IOException.class})
    public @ResponseBody ErrorEntity handlerException(Object exception) {

        if (exception instanceof UserNotFoundException) {
            UserNotFoundException userNotFoundEx = (UserNotFoundException) exception;
            return new ErrorEntity("400", userNotFoundEx.getMessage());
        }

        if (exception instanceof UserMailInvalidException) {
            UserMailInvalidException emailInvalidEx = (UserMailInvalidException) exception;
            return new ErrorEntity("400", emailInvalidEx.getMessage());
        }


        if (exception instanceof StorageFileNotFoundException) {
            StorageFileNotFoundException storageNotFound = (StorageFileNotFoundException) exception;
            return new ErrorEntity("400", storageNotFound.getMessage());
        }

        if (exception instanceof IOException) {
            IOException ioException = (IOException) exception;
            return new ErrorEntity("400", ioException.getMessage());
        }

        if (exception instanceof StorageException) {
            StorageException storageException = (StorageException) exception;
            return new ErrorEntity("400", storageException.getMessage());
        }

        if (exception instanceof TestimonyNotFoundException) {
            TestimonyNotFoundException testmonyNotFoundEx = (TestimonyNotFoundException) exception;
            return new ErrorEntity("400", testmonyNotFoundEx.getMessage());
        }

        if (exception instanceof TestimonySlugInvalideException) {
            TestimonySlugInvalideException testimonySlugInvalidEx = (TestimonySlugInvalideException) exception;
            return new ErrorEntity("400", testimonySlugInvalidEx.getMessage());
        }

        if (exception instanceof EventNotFoundException) {
            EventNotFoundException eventNotFoundException = (EventNotFoundException) exception;
            return new ErrorEntity("400", eventNotFoundException.getMessage());
        }

        if (exception instanceof EventsSlugInvalidException) {
            EventsSlugInvalidException eventSlugInvalidEx = (EventsSlugInvalidException) exception;
            return new ErrorEntity("400", eventSlugInvalidEx.getMessage());
        }

        if (exception instanceof ServicesNotFoundException) {
            ServicesNotFoundException servicesNotFoundEx = (ServicesNotFoundException) exception;
            return new ErrorEntity("400", servicesNotFoundEx.getMessage());
        }

        if (exception instanceof ServicesSlugInvalidException) {
            ServicesSlugInvalidException servicesSlugInvalidEx = (ServicesSlugInvalidException) exception;
            return new ErrorEntity("400", servicesSlugInvalidEx.getMessage());
        }

        if (exception instanceof ValidationCodeNotExist) {
            ValidationCodeNotExist validationCodeNotExist = (ValidationCodeNotExist) exception;
            return new ErrorEntity("400", validationCodeNotExist.getMessage());
        }

        if (exception instanceof BearerTokenNotFoundException) {
            BearerTokenNotFoundException bearerTokenNotFoundEx = (BearerTokenNotFoundException) exception;
            return new ErrorEntity("400", bearerTokenNotFoundEx.getMessage());
        }

        if (exception instanceof TrainingNotFoundException) {
            TrainingNotFoundException trainingNotFoundEx = (TrainingNotFoundException) exception;
            return new ErrorEntity("400", trainingNotFoundEx.getMessage());
        }

        if (exception instanceof TrainingSlugInvalidException) {
            TrainingSlugInvalidException trainingSlugInvalidEx = (TrainingSlugInvalidException) exception;
            return new ErrorEntity("400", trainingSlugInvalidEx.getMessage());
        }
        return null;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UsersAdminException.class})
    public @ResponseBody ErrorEntity handlerUnauthorizedException(Object exception) {

        if (exception instanceof UsersAdminException) {
            UsersAdminException usersAdminEx = (UsersAdminException) exception;
            return new ErrorEntity("401", usersAdminEx.getMessage());
        }
        return null;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({UserMailUsedException.class,
            TestimonySlugUsedException.class,
            EventsSlugUsedException.class,
            ServicesSlugUsedException.class,
            TrainingSlugUsedException.class})
    public @ResponseBody
    ErrorEntity handlerConflictException(Object exception) {
        if (exception instanceof UserMailUsedException) {
            UserMailUsedException emailUsedEx = (UserMailUsedException) exception;
            return new ErrorEntity("409", emailUsedEx.getMessage());
        }

        if (exception instanceof TestimonySlugUsedException) {
            TestimonySlugUsedException testimonySlugUsedEx = (TestimonySlugUsedException) exception;
            return new ErrorEntity("409", testimonySlugUsedEx.getMessage());
        }

        if (exception instanceof EventsSlugUsedException) {
            EventsSlugUsedException eventSlugUsedEx = (EventsSlugUsedException) exception;
            return new ErrorEntity("409", eventSlugUsedEx.getMessage());
        }

        if (exception instanceof ServicesSlugUsedException) {
            ServicesSlugUsedException ServicesSlugUsedEx = (ServicesSlugUsedException) exception;
            return new ErrorEntity("409", ServicesSlugUsedEx.getMessage());
        }

        if (exception instanceof CodeValidationException) {
            CodeValidationException codeValidationEx = (CodeValidationException) exception;
            return new ErrorEntity("409", codeValidationEx.getMessage());
        }

        if (exception instanceof TrainingSlugUsedException) {
            TrainingSlugUsedException trainingSlugUsedEx = (TrainingSlugUsedException) exception;
            return new ErrorEntity("409", trainingSlugUsedEx.getMessage());
        }
        return null;
    }

}
