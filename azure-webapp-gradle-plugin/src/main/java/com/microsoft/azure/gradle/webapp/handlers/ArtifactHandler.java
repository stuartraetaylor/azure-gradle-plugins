package com.microsoft.azure.gradle.webapp.handlers;

import com.microsoft.azure.gradle.webapp.configuration.DeployTarget;

public interface ArtifactHandler {
    void publish(DeployTarget deployTarget) throws Exception;
}
