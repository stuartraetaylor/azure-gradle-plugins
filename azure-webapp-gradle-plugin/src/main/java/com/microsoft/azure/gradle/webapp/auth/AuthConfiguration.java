package com.microsoft.azure.gradle.webapp.auth;

import com.microsoft.azure.gradle.webapp.configuration.Authentication;

public interface AuthConfiguration {

    String getSubscriptionId();

    String getUserAgent();

    Authentication getAuthenticationSettings();
}
