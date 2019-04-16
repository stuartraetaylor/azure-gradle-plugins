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
package com.microsoft.azure.gradle.functions.handlers;

import com.microsoft.azure.gradle.functions.FunctionsTask;
import com.microsoft.azure.gradle.functions.helpers.FTPUploader;
import com.microsoft.azure.management.appservice.FunctionApp;
import com.microsoft.azure.management.appservice.PublishingProfile;

public class FTPArtifactHandlerImpl implements ArtifactHandler {
    private static final String DEFAULT_FUNCTION_ROOT = "/site/wwwroot";
    private static final int DEFAULT_MAX_RETRY_TIMES = 3;

    private FunctionsTask functionsTask;

    public FTPArtifactHandlerImpl(final FunctionsTask functionsTask) {
        this.functionsTask = functionsTask;
    }

    @Override
    public void publish() throws Exception {
        final FTPUploader uploader = getUploader();
        final FunctionApp app = functionsTask.getFunctionApp();
        final PublishingProfile profile = app.getPublishingProfile();
        final String serverUrl = profile.ftpUrl().split("/", 2)[0];

        uploader.uploadDirectoryWithRetries(
                serverUrl,
                profile.ftpUsername(),
                profile.ftpPassword(),
                functionsTask.getDeploymentStageDirectory(),
                DEFAULT_FUNCTION_ROOT,
                DEFAULT_MAX_RETRY_TIMES);

        app.syncTriggers();
    }

    protected FTPUploader getUploader() {
        return new FTPUploader(functionsTask.getLogger());
    }
}
