package com.swe599.ramp.exceptions;

public class UserAlreadyRegisteredException extends RuntimeException{
    public UserAlreadyRegisteredException(String email) {
        super("User is already registered by email: " + email);
    }
}
