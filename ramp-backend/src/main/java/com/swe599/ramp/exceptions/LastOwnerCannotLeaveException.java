package com.swe599.ramp.exceptions;

public class LastOwnerCannotLeaveException extends RuntimeException {
    public LastOwnerCannotLeaveException() {
        super("There mest be at least 1 owner in a community");
    }
}
