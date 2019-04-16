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
import com.microsoft.azure.functions.annotation.TableInput;
import com.microsoft.azure.functions.annotation.TableOutput;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TableBinding extends StorageBaseBinding {
    public static final String TABLE = "table";

    private String tableName = "";

    private String partitionKey = "";

    private String rowKey = "";

    private String filter = "";

    private String take = "";

    public TableBinding(final TableInput tableInput) {
        super(tableInput.name(), TABLE, Direction.IN, tableInput.dataType());

        tableName = tableInput.tableName();
        partitionKey = tableInput.partitionKey();
        rowKey = tableInput.rowKey();
        filter = tableInput.filter();
        take = tableInput.take();
        setConnection(tableInput.connection());
    }

    public TableBinding(final TableOutput tableOutput) {
        super(tableOutput.name(), TABLE, Direction.OUT, tableOutput.dataType());

        tableName = tableOutput.tableName();
        partitionKey = tableOutput.partitionKey();
        rowKey = tableOutput.rowKey();
        setConnection(tableOutput.connection());
    }

    @JsonGetter
    public String getTableName() {
        return tableName;
    }

    @JsonGetter
    public String getPartitionKey() {
        return partitionKey;
    }

    @JsonGetter
    public String getRowKey() {
        return rowKey;
    }

    @JsonGetter
    public String getFilter() {
        return filter;
    }

    @JsonGetter
    public String getTake() {
        return take;
    }
}
