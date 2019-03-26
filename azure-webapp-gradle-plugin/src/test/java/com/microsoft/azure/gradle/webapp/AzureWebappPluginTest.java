package com.microsoft.azure.gradle.webapp;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AzureWebappPluginTest {
    @Test
    public void greeterPluginAddsGreetingTaskToProject() {
        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("com.microsoft.azure.gradle.webapp");

        assertTrue(project.getTasks().findByPath("azureWebappDeploy") instanceof DeployTask);
    }
}
