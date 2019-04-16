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
package com.microsoft.azure.gradle.webapp.configuration;

import com.microsoft.azure.management.appservice.AppSetting;
import com.microsoft.azure.management.appservice.PublishingProfile;
import com.microsoft.azure.management.appservice.WebAppBase;

import java.io.File;
import java.util.Map;

public class DeployTarget<T extends WebAppBase> {
    protected DeployTargetType type;
    protected T app;

    public DeployTarget(final T app, final DeployTargetType type) {
        this.app = app;
        this.type = type;
    }

    public PublishingProfile getPublishingProfile() {
        return app.getPublishingProfile();
    }

    public String getName() {
        return app.name();
    }

    public String getType() {
        return type.toString();
    }

    public String getDefaultHostName() {
        return app.defaultHostName();
    }

    public Map<String, AppSetting> getAppSettings() {
        return app.getAppSettings();
    }

    public void zipDeploy(final File file) {
        app.zipDeploy(file);
    }

    public void msDeploy(final String packageUri, final boolean deleteExistingDeploymentSlot) {
        app.deploy()
                .withPackageUri(packageUri)
                .withExistingDeploymentsDeleted(deleteExistingDeploymentSlot)
                .execute();
    }

    public T getApp() {
        return this.app;
    }
}
