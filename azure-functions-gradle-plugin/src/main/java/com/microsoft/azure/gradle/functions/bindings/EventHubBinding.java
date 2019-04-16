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
import com.microsoft.azure.functions.annotation.EventHubOutput;
import com.microsoft.azure.functions.annotation.EventHubTrigger;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EventHubBinding extends BaseBinding {
    public static final String EVENT_HUB_TRIGGER = "eventHubTrigger";
    public static final String EVENT_HUB = "eventHub";

    private String eventHubName = "";

    private String consumerGroup = "";

    private String connection = "";

    public EventHubBinding(final EventHubTrigger eventHubTrigger) {
        super(eventHubTrigger.name(), EVENT_HUB_TRIGGER, Direction.IN, eventHubTrigger.dataType());

        eventHubName = eventHubTrigger.eventHubName();
        consumerGroup = eventHubTrigger.consumerGroup();
        connection = eventHubTrigger.connection();
    }

    public EventHubBinding(final EventHubOutput eventHubOutput) {
        super(eventHubOutput.name(), EVENT_HUB, Direction.OUT, eventHubOutput.dataType());

        eventHubName = eventHubOutput.eventHubName();
        connection = eventHubOutput.connection();
    }

    @JsonGetter("path")
    public String getEventHubName() {
        return eventHubName;
    }

    @JsonGetter
    public String getConsumerGroup() {
        return consumerGroup;
    }

    @JsonGetter
    public String getConnection() {
        return connection;
    }
}
