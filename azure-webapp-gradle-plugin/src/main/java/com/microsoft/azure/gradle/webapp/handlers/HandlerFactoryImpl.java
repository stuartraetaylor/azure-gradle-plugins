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
import com.microsoft.azure.gradle.webapp.configuration.AppService;
import com.microsoft.azure.gradle.webapp.configuration.AppServiceType;
import com.microsoft.azure.gradle.webapp.configuration.Deployment;
import com.microsoft.azure.gradle.webapp.configuration.DockerImageType;
import com.microsoft.azure.gradle.webapp.helpers.WebAppUtils;
import org.gradle.api.GradleException;
import org.gradle.api.Project;

import static com.microsoft.azure.gradle.webapp.helpers.CommonStringTemplates.APP_SERVICE_PROPERTY_MISSING_TEMPLATE;
import static com.microsoft.azure.gradle.webapp.helpers.CommonStringTemplates.PROPERTY_MISSING_TEMPLATE;
import static com.microsoft.azure.gradle.webapp.helpers.CommonStringTemplates.UNKNOWN_VALUE_TEMPLATE;

public class HandlerFactoryImpl extends HandlerFactory {
    @Override
    public RuntimeHandler getRuntimeHandler(final DeployTask task) throws GradleException {
        AppService appService = task.getAzureWebAppExtension().getAppService();
        if (appService == null) {
            return new NullRuntimeHandlerImpl();
        }

        if (appService.getType() == AppServiceType.WINDOWS) {
            return new WindowsRuntimeHandlerImpl(task);
        }

        if (appService.getType() == AppServiceType.LINUX) {
            return new LinuxRuntimeHandlerImpl(task);
        }

        if (appService.getType() == AppServiceType.DOCKER) {
            final DockerImageType imageType = WebAppUtils.getDockerImageTypeFromName(appService);
            task.getLogger().quiet("imageType: " + imageType);
            switch (imageType) {
                case PUBLIC_DOCKER_HUB:
                    return new PublicDockerHubRuntimeHandlerImpl(task);
                case PRIVATE_DOCKER_HUB:
                    return new PrivateDockerHubRuntimeHandlerImpl(task);
                case PRIVATE_REGISTRY:
                    return new PrivateRegistryRuntimeHandlerImpl(task);
                case NONE:
                default:
                    throw new GradleException(
                            String.format(APP_SERVICE_PROPERTY_MISSING_TEMPLATE, "appService.imageName", "DOCKER"));
            }
        }
        throw new GradleException(String.format(UNKNOWN_VALUE_TEMPLATE, "appService.type"));
    }

    @Override
    public SettingsHandler getSettingsHandler(final Project project) throws GradleException {
        return new SettingsHandlerImpl(project);
    }

    @Override
    public ArtifactHandler getArtifactHandler(final DeployTask task) throws GradleException {
        Deployment deployment = task.getAzureWebAppExtension().getDeployment();
        if (deployment == null && !AppServiceType.DOCKER.equals(task.getAzureWebAppExtension().getAppService().getType())) {
            task.getLogger().quiet("No deployment configured, exit.");
            return null;
        }
        if (deployment.getType() == null) {
            throw new GradleException(String.format(PROPERTY_MISSING_TEMPLATE, "deployment.type"));
        }
        switch (deployment.getType()) {
            case NONE:
                if (task.getAzureWebAppExtension().getAppService().getType() != AppServiceType.DOCKER) {
                    throw new GradleException(String.format(PROPERTY_MISSING_TEMPLATE, "deployment.type"));
                }
                return null;
            case UNKNOWN:
                throw new GradleException(String.format(UNKNOWN_VALUE_TEMPLATE, "deployment.type"));
            case WAR:
                return new WarArtifactHandlerImpl(task);
            case EAR:
                return new EarArtifactHandlerImpl(task);
            case ZIP:
                return new ZipArtifactHandlerImpl(task);
            case JAR:
                return new JarArtifactHandlerImpl(task);
            case FTP:
            default:
                return new FTPArtifactHandlerImpl(task);
        }
    }
}
