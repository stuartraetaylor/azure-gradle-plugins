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
import com.microsoft.azure.functions.annotation.SendGridOutput;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SendGridBinding extends BaseBinding {
    public static final String SEND_GRID = "sendGrid";

    private String apiKey = "";

    private String to = "";

    private String from = "";

    private String subject = "";

    private String text = "";

    public SendGridBinding(final SendGridOutput sendGridOutput) {
        super(sendGridOutput.name(), SEND_GRID, Direction.OUT, sendGridOutput.dataType());

        apiKey = sendGridOutput.apiKey();
        to = sendGridOutput.to();
        from = sendGridOutput.from();
        subject = sendGridOutput.subject();
        text = sendGridOutput.text();
    }

    @JsonGetter
    public String getApiKey() {
        return apiKey;
    }

    @JsonGetter
    public String getTo() {
        return to;
    }

    @JsonGetter
    public String getFrom() {
        return from;
    }

    @JsonGetter
    public String getSubject() {
        return subject;
    }

    @JsonGetter
    public String getText() {
        return text;
    }
}
