package com.teamtreehouse.blog.model;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Resource not found");
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String resource, String identifier) {
        super(String.format("%s with identifier '%s' not found", resource, identifier));
    }
}
