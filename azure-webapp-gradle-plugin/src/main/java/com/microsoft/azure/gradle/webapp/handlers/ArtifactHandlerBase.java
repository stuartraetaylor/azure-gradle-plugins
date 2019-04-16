/*
 * MIT License
 *
 * Copyright (c) 2017-2019 Elena Lakhno
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.microsoft.azure.gradle.webapp.handlers;

import com.microsoft.azure.gradle.webapp.DeployTask;
import com.microsoft.azure.gradle.webapp.configuration.FTPResource;
import org.apache.commons.io.FileUtils;
import org.gradle.api.GradleException;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public abstract class ArtifactHandlerBase implements ArtifactHandler {
    protected static final String DEPLOY_START = "Trying to deploy artifact to %s...";
    protected static final String DEPLOY_FINISH = "Successfully deployed the artifact to https://%s";

    protected DeployTask task;

    protected ArtifactHandlerBase(@Nonnull final DeployTask task) {
        this.task = task;
    }

    protected void assureStagingDirectoryNotEmpty() throws GradleException {
        final String stagingDirectoryPath = getDeploymentStagingDirectoryPath();
        final File stagingDirectory = new File(stagingDirectoryPath);
        final File[] files = stagingDirectory.listFiles();
        if (!stagingDirectory.exists() || !stagingDirectory.isDirectory() || files == null || files.length == 0) {
            throw new GradleException(String.format("Staging directory: '%s' is empty.",
                    stagingDirectory.getAbsolutePath()));
        }
    }

    protected String getDeploymentStagingDirectoryPath() {
        String stageDirectory = Paths.get(getBuildDirectoryAbsolutePath(),
                "azure-webapps",
                task.getAzureWebAppExtension().getAppName()).toString();
        task.getLogger().quiet(stageDirectory);
        return stageDirectory;
    }

    protected void prepareResources() throws IOException {
        final List<FTPResource> resources = task.getAzureWebAppExtension().getDeployment().getResources();

        if (resources != null && !resources.isEmpty()) {
            resources.forEach(item -> {
                doCopyResourceToStageDirectory(item);
            });
        }
    }

    private void doCopyResourceToStageDirectory(final FTPResource resource) {
        File file  = new File(resource.getSourcePath());
        if (!file.exists()) {
            task.getLogger().quiet(resource.getSourcePath() + " configured in deployment.resources does not exist.");
            return;
        }
        File destination = new File(Paths.get(getDeploymentStagingDirectoryPath(), resource.getTargetPath()).toString());
        try {
            if (file.isFile()) {
                FileUtils.copyFileToDirectory(file, destination);
            } else if (file.isDirectory()) {
                FileUtils.copyDirectoryToDirectory(file, destination);
            }
        } catch (IOException e) {
            task.getLogger().quiet("exception when copy resource: " + e.getLocalizedMessage());
        }
    }

    public String getBuildDirectoryAbsolutePath() {
        return task.getProject().getBuildDir().getAbsolutePath();
    }
}
