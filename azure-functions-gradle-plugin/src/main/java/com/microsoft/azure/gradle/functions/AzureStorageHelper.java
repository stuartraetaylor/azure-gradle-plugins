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
package com.microsoft.azure.gradle.functions;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.io.File;
import java.io.FileInputStream;

public class AzureStorageHelper {
    public static String uploadFileAsBlob(final File fileToUpload, final CloudStorageAccount storageAccount,
                                          final String containerName, final String blobName) throws Exception {
        final CloudBlobContainer blobContainer = getBlobContainer(storageAccount, containerName);
        blobContainer.createIfNotExists(BlobContainerPublicAccessType.BLOB, null, null);

        final CloudBlockBlob blob = blobContainer.getBlockBlobReference(blobName);
        blob.upload(new FileInputStream(fileToUpload), fileToUpload.length());
        return blob.getUri().toString();
    }

    public static void deleteBlob(final CloudStorageAccount storageAccount, final String containerName,
                                  final String blobName) throws Exception {
        final CloudBlobContainer blobContainer = getBlobContainer(storageAccount, containerName);
        if (blobContainer.exists()) {
            final CloudBlockBlob blob = blobContainer.getBlockBlobReference(blobName);
            blob.deleteIfExists();
        }
    }

    private static CloudBlobContainer getBlobContainer(final CloudStorageAccount storageAccount,
                                                       final String containerName) throws Exception {
        final CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
        return blobClient.getContainerReference(containerName);
    }
}
