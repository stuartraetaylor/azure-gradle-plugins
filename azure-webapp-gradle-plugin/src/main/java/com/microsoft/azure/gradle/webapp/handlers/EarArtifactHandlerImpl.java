/*
 * MIT License
 *
 * Copyright (c) 2019 Stuart Taylor
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

import static com.microsoft.azure.management.appservice.RuntimeStack.WILDFLY_14_JRE8;

import com.google.common.io.Files;
import com.microsoft.azure.gradle.webapp.AzureWebAppExtension;
import com.microsoft.azure.gradle.webapp.DeployTask;
import com.microsoft.azure.gradle.webapp.configuration.DeployTarget;
import com.microsoft.azure.gradle.webapp.configuration.Deployment;
import com.microsoft.azure.gradle.webapp.configuration.FTPResource;
import org.apache.commons.lang3.StringUtils;
import org.gradle.api.GradleException;

import java.io.IOException;
import java.io.File;
import java.util.List;

public class EarArtifactHandlerImpl extends ZipArtifactHandlerImpl {

    private static final String remoteDir = "archives";

    private String earFile;

    public EarArtifactHandlerImpl(DeployTask task) {
        super(task);
    }

    @Override
    public void prepareResources() throws GradleException, IOException {
        AzureWebAppExtension extension = task.getAzureWebAppExtension();

        String runtimeStack = extension.getAppService().getRuntimeStack();
        if (!WILDFLY_14_JRE8.toString().equalsIgnoreCase(runtimeStack))
            throw new GradleException("Runtime stack not supported: " + runtimeStack);

        List<FTPResource> resources = extension.getDeployment().getResources();
        if (!resources.isEmpty())
            throw new GradleException("Webapp resources {} block not supported");

        resources.add(
            new FTPResource(earFile, remoteDir)
        );

        super.prepareResources();
    }

    @Override
    public void publish(DeployTarget target) throws GradleException, IOException {
        Deployment deployment = task.getAzureWebAppExtension().getDeployment();
        this.earFile = deployment.getEarFile();

        if (StringUtils.isEmpty(earFile))
           this.earFile = task.getProject().getTasks().getByPath("ear").getOutputs().getFiles().getAsPath();

        File targetFile = new File(earFile);
        ensureEARFileExists(targetFile);

        task.getLogger().quiet("EAR file: " + earFile);
        super.publish(target);
    }

    private void ensureEARFileExists(File targetFile) throws GradleException {
        String fileName = targetFile.getName();
        if (!Files.getFileExtension(fileName).equalsIgnoreCase("ear")
                || !targetFile.exists() || !targetFile.isFile()) {

            throw new GradleException("Not an EAR file: " + fileName);
        }
    }

}
