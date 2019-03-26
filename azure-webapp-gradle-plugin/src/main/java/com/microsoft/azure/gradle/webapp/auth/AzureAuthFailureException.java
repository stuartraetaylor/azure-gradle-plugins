package com.microsoft.azure.gradle.webapp.auth;

public class AzureAuthFailureException extends Exception {
    public AzureAuthFailureException(String message) {
        super(message);
    }
}
