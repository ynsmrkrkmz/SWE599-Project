package com.swe599.ramp.exceptions;

public class UserAlreadyAMemberException extends RuntimeException {
    public UserAlreadyAMemberException(String email) {
        super(String.format("Invited user by %s already exists", email));
    }
}
