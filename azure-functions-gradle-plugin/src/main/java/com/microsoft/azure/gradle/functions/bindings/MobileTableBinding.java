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
import com.microsoft.azure.functions.annotation.MobileTableInput;
import com.microsoft.azure.functions.annotation.MobileTableOutput;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MobileTableBinding extends BaseBinding {
    public static final String MOBILE_TABLE = "mobileTable";

    private String tableName = "";

    private String id = "";

    private String connection = "";

    private String apiKey = "";

    public MobileTableBinding(final MobileTableInput tableInput) {
        super(tableInput.name(), MOBILE_TABLE, Direction.IN, tableInput.dataType());

        tableName = tableInput.tableName();
        id = tableInput.id();
        connection = tableInput.connection();
        apiKey = tableInput.apiKey();
    }

    public MobileTableBinding(final MobileTableOutput tableOutput) {
        super(tableOutput.name(), MOBILE_TABLE, Direction.OUT, tableOutput.dataType());

        tableName = tableOutput.tableName();
        connection = tableOutput.connection();
        apiKey = tableOutput.apiKey();
    }

    @JsonGetter
    public String getTableName() {
        return tableName;
    }

    @JsonGetter
    public String getId() {
        return id;
    }

    @JsonGetter
    public String getConnection() {
        return connection;
    }

    @JsonGetter
    public String getApiKey() {
        return apiKey;
    }
}
