package uz.javacourse.jgcp.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(Long id) {
        super("User not found with id: " + id, HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String field, String value) {
        super("User not found with " + field + ": " + value, HttpStatus.NOT_FOUND);
    }
}
