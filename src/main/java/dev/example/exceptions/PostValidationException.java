package dev.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PostValidationException extends RuntimeException {

    public PostValidationException(String message, Object... args) {
        super(message.formatted(args));
    }
}
