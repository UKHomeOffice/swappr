package uk.gov.homeofficedigital.swappr.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public abstract class NotFoundException extends RuntimeException {
}
