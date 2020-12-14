package com.dsinnovators.devprofilesbackend.utils;

public class DeveloperNotFoundException extends Exception {
    public DeveloperNotFoundException(Long id) {
        super("Developer Not Found with ID: " + id);
    }
}
