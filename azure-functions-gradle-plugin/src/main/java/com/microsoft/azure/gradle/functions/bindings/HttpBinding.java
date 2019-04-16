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
import com.microsoft.azure.functions.annotation.HttpOutput;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Locale;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HttpBinding extends BaseBinding {
    public static final String HTTP_TRIGGER = "httpTrigger";
    public static final String HTTP = "http";

    private String route = "";

    private String webHookType = "";

    private String authLevel = "";

    private String[] methods = {};

    public HttpBinding(final HttpTrigger httpTrigger) {
        super(httpTrigger.name(), HTTP_TRIGGER, Direction.IN, httpTrigger.dataType());

        route = httpTrigger.route();
        authLevel = httpTrigger.authLevel().toString().toLowerCase(Locale.ENGLISH);
        methods = httpTrigger.methods();
        webHookType = httpTrigger.webHookType();
    }

    public HttpBinding(final HttpOutput httpOutput) {
        super(httpOutput.name(), HTTP, Direction.OUT, httpOutput.dataType());
    }

    public HttpBinding() {
        super("$return", HTTP, Direction.OUT, "");
    }

    @JsonGetter
    public String getRoute() {
        return route;
    }

    @JsonGetter
    public String getWebHookType() {
        return webHookType;
    }

    @JsonGetter
    public String getAuthLevel() {
        return authLevel;
    }

    @JsonGetter
    public String[] getMethods() {
        return methods.clone();
    }
}
