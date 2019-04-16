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
import com.microsoft.azure.functions.annotation.ServiceBusQueueOutput;
import com.microsoft.azure.functions.annotation.ServiceBusQueueTrigger;
import com.microsoft.azure.functions.annotation.ServiceBusTopicOutput;
import com.microsoft.azure.functions.annotation.ServiceBusTopicTrigger;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ServiceBusBinding extends BaseBinding {
    public static final String SERVICE_BUS_TRIGGER = "serviceBusTrigger";
    public static final String SERVICE_BUS = "serviceBus";

    private String queueName = "";

    private String topicName = "";

    private String subscriptionName = "";

    private String connection = "";

    private String access = "";

    public ServiceBusBinding(final ServiceBusQueueTrigger queueTrigger) {
        super(queueTrigger.name(), SERVICE_BUS_TRIGGER, Direction.IN, queueTrigger.dataType());

        queueName = queueTrigger.queueName();
        connection = queueTrigger.connection();
        access = queueTrigger.access().toString();
    }

    public ServiceBusBinding(final ServiceBusTopicTrigger topicTrigger) {
        super(topicTrigger.name(), SERVICE_BUS_TRIGGER, Direction.IN, topicTrigger.dataType());

        topicName = topicTrigger.topicName();
        subscriptionName = topicTrigger.subscriptionName();
        connection = topicTrigger.connection();
        access = topicTrigger.access().toString();
    }

    public ServiceBusBinding(final ServiceBusQueueOutput queueOutput) {
        super(queueOutput.name(), SERVICE_BUS, Direction.OUT, queueOutput.dataType());

        queueName = queueOutput.queueName();
        connection = queueOutput.connection();
        access = queueOutput.access().toString();
    }

    public ServiceBusBinding(final ServiceBusTopicOutput topicOutput) {
        super(topicOutput.name(), SERVICE_BUS, Direction.OUT, topicOutput.dataType());

        topicName = topicOutput.topicName();
        subscriptionName = topicOutput.subscriptionName();
        connection = topicOutput.connection();
        access = topicOutput.access().toString();
    }

    @JsonGetter
    public String getQueueName() {
        return queueName;
    }

    @JsonGetter
    public String getTopicName() {
        return topicName;
    }

    @JsonGetter
    public String getSubscriptionName() {
        return subscriptionName;
    }

    @JsonGetter
    public String getAccess() {
        return access;
    }

    @JsonGetter
    public String getConnection() {
        return connection;
    }
}
