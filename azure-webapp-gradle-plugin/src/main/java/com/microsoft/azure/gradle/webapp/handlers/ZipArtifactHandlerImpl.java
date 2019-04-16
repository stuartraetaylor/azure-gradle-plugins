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
import com.microsoft.azure.gradle.webapp.configuration.DeployTarget;
import org.gradle.api.GradleException;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;

public class ZipArtifactHandlerImpl extends ArtifactHandlerBase {
    private static final int DEFAULT_MAX_RETRY_TIMES = 3;

    public ZipArtifactHandlerImpl(final DeployTask task) {
        super(task);
    }

    @Override
    public void publish(DeployTarget target) throws GradleException, IOException {
        prepareResources();
        assureStagingDirectoryNotEmpty();

        File zipFile = getZipFile();
        task.getLogger().info(String.format(DEPLOY_START, target.getName()));

        // Add retry logic here to avoid Kudu's socket timeout issue.
        int retryCount = 0;
        while (retryCount < DEFAULT_MAX_RETRY_TIMES) {
            retryCount += 1;
            try {
                target.zipDeploy(zipFile);
                task.getLogger().quiet(String.format(DEPLOY_FINISH, target.getDefaultHostName()));
                return;
            } catch (Exception e) {
                task.getLogger().quiet(
                        String.format("Exception occurred when deploying the zip package: %s, " +
                                              "retrying immediately (%d/%d)", e.getMessage(), retryCount, DEFAULT_MAX_RETRY_TIMES));
            }
        }
        target.zipDeploy(getZipFile());
    }

    protected File getZipFile() {
        final String stagingDirectoryPath = getDeploymentStagingDirectoryPath();
        final File zipFile = new File(stagingDirectoryPath + ".zip");
        final File stagingDirectory = new File(stagingDirectoryPath);

        ZipUtil.pack(stagingDirectory, zipFile);
        return zipFile;
    }
}
