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

import com.google.common.io.Files;
import com.microsoft.azure.gradle.webapp.DeployTask;
import com.microsoft.azure.gradle.webapp.configuration.DeployTarget;
import com.microsoft.azure.gradle.webapp.configuration.Deployment;
import org.apache.commons.lang3.StringUtils;
import org.gradle.api.GradleException;

import java.io.File;

public class WarArtifactHandlerImpl implements ArtifactHandler {
    private static final String FILE_IS_NOT_WAR = "The deployment file is not a war typed file.";
    private static final String FIND_WAR_FILE_FAIL = "Failed to find the war file: '%s'";
    private static final int DEFAULT_MAX_RETRY_TIMES = 3;
    private static final String UPLOAD_FAILURE =
            "Failed to deploy the war file to server, retrying immediately (%d/%d)";

    private DeployTask task;

    public WarArtifactHandlerImpl(final DeployTask task) {
        this.task = task;
    }

    @Override
    public void publish(DeployTarget target) throws GradleException {
        Deployment deployment = task.getAzureWebAppExtension().getDeployment();
        if (deployment == null) {
            return;
        }
        String warFile = deployment.getWarFile();
        if (StringUtils.isEmpty(warFile)) {
            warFile = task.getProject().getTasks().getByPath("war").getOutputs().getFiles().getAsPath();
        }

        File targetFile = new File(warFile);
        assureWarFileExisted(targetFile);

        String contextPath = task.getAzureWebAppExtension().getDeployment().getContextPath();
        task.getLogger().quiet("War name is: " + warFile);
        int retryCount = 0;
        task.getLogger().quiet("Starting to deploy the war file...");
        while (retryCount++ < DEFAULT_MAX_RETRY_TIMES) {
            try {
                task.getWebApp().warDeploy(targetFile, contextPath);
                return;
            } catch (Exception e) {
                task.getLogger().quiet(String.format(UPLOAD_FAILURE, retryCount, DEFAULT_MAX_RETRY_TIMES));
            }
        }
    }

    private void assureWarFileExisted(File war) throws GradleException {
        if (!Files.getFileExtension(war.getName()).equalsIgnoreCase("war")) {
            throw new GradleException(FILE_IS_NOT_WAR);
        }

        if (!war.exists() || !war.isFile()) {
            throw new GradleException(String.format(FIND_WAR_FILE_FAIL, war.getAbsolutePath()));
        }
    }
}
