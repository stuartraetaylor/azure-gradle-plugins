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
package com.microsoft.azure.gradle.functions.bindings;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.microsoft.azure.functions.annotation.NotificationHubOutput;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NotificationHubBinding extends BaseBinding {
    public static final String NOTIFICATION_HUB = "notificationHub";

    private String tagExpression = "";

    private String hubName = "";

    private String connection = "";

    private String platform = "";

    public NotificationHubBinding(final NotificationHubOutput hubOutput) {
        super(hubOutput.name(), NOTIFICATION_HUB, Direction.OUT, hubOutput.dataType());

        tagExpression = hubOutput.tagExpression();
        hubName = hubOutput.hubName();
        connection = hubOutput.connection();
        platform = hubOutput.platform();
    }

    @JsonGetter
    public String getTagExpression() {
        return tagExpression;
    }

    @JsonGetter
    public String getHubName() {
        return hubName;
    }

    @JsonGetter
    public String getConnection() {
        return connection;
    }

    @JsonGetter
    public String getPlatform() {
        return platform;
    }
}
