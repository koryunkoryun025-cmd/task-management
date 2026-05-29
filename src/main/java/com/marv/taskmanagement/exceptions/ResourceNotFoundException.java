package com.marv.taskmanagement.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super((message));
    }
}
