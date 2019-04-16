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
package com.microsoft.azure.gradle.webapp.helpers;

public final class CommonStringTemplates {
    public static final String PROPERTY_MISSING_TEMPLATE = "'%s' is not configured in azure-functions-gradle-plugin.gradle.";
    public static final String UNKNOWN_VALUE_TEMPLATE = "Unknown value: '%s' in azure-functions-gradle-plugin.gradle";
    public static final String APP_SERVICE_PROPERTY_MISSING_TEMPLATE =
            "'%s' is required in azure-functions-gradle-plugin.gradle when appService.type is '%s'";
    public static final String NOT_COMPATIBLE_WEBAPP_TEMPLATE =
            "azureWebApp configured an existing web app in azure-functions-gradle-plugin.gradle and it is not '%s' Web App. "
                    + "Please align appService configuration with a compatible web app. ";
}
