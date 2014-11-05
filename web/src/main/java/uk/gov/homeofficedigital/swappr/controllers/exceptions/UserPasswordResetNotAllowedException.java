package uk.gov.homeofficedigital.swappr.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserPasswordResetNotAllowedException extends RuntimeException {
    public UserPasswordResetNotAllowedException(String message) {
        super(message);
    }
}
