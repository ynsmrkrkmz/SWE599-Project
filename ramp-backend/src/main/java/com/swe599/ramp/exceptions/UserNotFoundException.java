package com.swe599.ramp.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("User could not found by email: " + email);
    }
}
