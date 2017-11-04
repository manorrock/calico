/*
 *  Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.files.rest;

import com.manorrock.files.model.DirectoryModel;
import com.manorrock.files.model.FileAttributesModel;
import com.manorrock.files.model.FileModel;
import com.manorrock.rest.RestResponse;
import com.manorrock.rest.RestResponseBuilder;
import com.manorrock.rest.RestResponseBuilderFactory;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A file resource.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class FileResource {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(FileResource.class.getName());

    /**
     * Stores the root directory.
     */
    private final File rootDirectory;

    /**
     * Constructor.
     */
    public FileResource() {
        String rootDirectoryFilename = System.getenv("ROOT_DIRECTORY");
        if (rootDirectoryFilename == null) {
            rootDirectoryFilename = System.getProperty("ROOT_DIRECTORY",
                    System.getProperty("user.home") + "/.manorrock/files/rest");
        }
        rootDirectory = new File(rootDirectoryFilename);
        if (!rootDirectory.exists()) {
            rootDirectory.mkdirs();
        }
    }

    /**
     * Create the file specified by the path.
     *
     * @param path the path.
     * @param inputStream the data as an input stream.
     * @return the response.
     */
    public RestResponse createFile(String path, boolean directory, InputStream inputStream) {
        RestResponse result = null;
        File file = new File(rootDirectory, path);
        if (file.exists()) {
            RestResponseBuilder builder = RestResponseBuilderFactory.produce();
            result = builder.status(400).build();
        } else {
            if (directory) {
                file.mkdirs();
                RestResponseBuilder builder = RestResponseBuilderFactory.produce();
                result = builder.status(201).header("Location", path).build();
            } else {
                FileOutputStream fileOutput = null;
                if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                    RestResponseBuilder builder = RestResponseBuilderFactory.produce();
                    result = builder.status(500).build();
                } else {
                    try {
                        try (BufferedInputStream bufferedInput = new BufferedInputStream(inputStream, 8192)) {
                            fileOutput = new FileOutputStream(file);
                            try (BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput, 8192)) {
                                byte[] buffer = new byte[8192];
                                int read;
                                while ((read = bufferedInput.read(buffer)) > 0) {
                                    bufferedOutput.write(buffer);
                                }
                                bufferedOutput.flush();
                            }
                            fileOutput.flush();
                            fileOutput.close();
                        }
                    } catch (IOException ioe) {
                        RestResponseBuilder builder = RestResponseBuilderFactory.produce();
                        result = builder.status(500).build();
                    } finally {
                        try {
                            if (fileOutput != null) {
                                fileOutput.close();
                            }
                        } catch (IOException ioe) {
                            RestResponseBuilder builder = RestResponseBuilderFactory.produce();
                            result = builder.status(500).build();
                        }
                    }
                    if (result == null) {
                        RestResponseBuilder builder = RestResponseBuilderFactory.produce();
                        result = builder.status(201).header("Location", path).build();
                    }
                }
            }
        }
        return result;
    }

    /**
     * Delete the file specified by the path.
     *
     * @param path the path.
     * @return the response.
     */
    public RestResponse deleteFile(String path) {
        RestResponse result;
        File file = new File(rootDirectory, path);
        if (file.delete()) {
            RestResponseBuilder builder = RestResponseBuilderFactory.produce();
            result = builder.status(200).build();
        } else {
            RestResponseBuilder builder = RestResponseBuilderFactory.produce();
            result = builder.status(500).build();
        }
        return result;
    }

    /**
     * Get the file.
     *
     * @param path the path.
     * @return the file content.
     */
    public Object getFile(String path) {
        LOGGER.log(Level.INFO, "Getting path: {0}", path);
        Object result;
        try {
            if ("/".equals(path)) {
                path = "";
            }
            File file = new File(rootDirectory, path);
            if (!file.exists()) {
                RestResponseBuilder builder = RestResponseBuilderFactory.produce();
                result = builder.status(404).build();
            } else if (!file.isDirectory()) {
                RestResponseBuilder builder = RestResponseBuilderFactory.produce();
                Path filePath = Paths.get(file.toURI());
                InputStream inputStream = Files.newInputStream(filePath);
                result = builder.status(200).contentType("application/octet-stream").
                        body(inputStream).build();
            } else {
                DirectoryModel model = new DirectoryModel();
                if (file.equals(rootDirectory)) {
                    model.setName("");
                } else {
                    model.setName(file.getAbsolutePath().substring(rootDirectory.getCanonicalPath().length() + 1));
                }
                ArrayList<FileModel> files = new ArrayList<>();
                File[] fileList = file.listFiles();
                Arrays.sort(fileList);
                for (File currentFile : fileList) {
                    String name = currentFile.getAbsolutePath().substring(file.getCanonicalPath().length() + 1);
                    FileAttributesModel attributes = (FileAttributesModel) getFileAttributes(path + "/" + name);
                    name = name.replaceAll("&", "&amp;");
                    boolean show = true;
                    if (System.getProperty("os.name").toLowerCase().contains("mac os")
                            && name.startsWith(".")) {
                        show = false;
                    }
                    if (show) {
                        FileModel fileModel = new FileModel();
                        fileModel.setName(name);
                        fileModel.setAttributes(attributes);
                        files.add(fileModel);
                    }
                }
                model.setFiles(files);
                result = model;
            }
        } catch (IOException ioe) {
            RestResponseBuilder builder = RestResponseBuilderFactory.produce();
            result = builder.status(500).build();
        }
        return result;
    }

    /**
     * Get the file attributes.
     *
     * @param path the path
     * @return the response.
     */
    public Object getFileAttributes(String path) {
        Object result;
        File file = new File(rootDirectory, path);
        if (!file.exists()) {
            RestResponseBuilder builder = RestResponseBuilderFactory.produce();
            result = builder.status(400).build();
        } else {
            try {
                FileAttributesModel model = new FileAttributesModel();
                Path filePath = file.toPath();
                BasicFileAttributes attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
                model.setCreated(attributes.creationTime().toMillis());
                model.setDirectory(file.isDirectory());
                model.setLastAccessed(attributes.lastAccessTime().toMillis());
                model.setLastModified(file.lastModified());
                model.setLength(file.length());
                model.setSymbolicLink(attributes.isSymbolicLink());
                result = model;
            } catch (IOException ioe) {
                RestResponseBuilder builder = RestResponseBuilderFactory.produce();
                result = builder.status(500).build();
            }
        }
        return result;
    }

    /**
     * Update the file specified by the path.
     *
     * @param path the path.
     * @param inputStream the data as an input stream.
     * @return the response.
     */
    public RestResponse updateFile(String path, InputStream inputStream) {
        RestResponse result = null;
        File file = new File(rootDirectory, path);
        if (!file.exists()) {
            RestResponseBuilder builder = RestResponseBuilderFactory.produce();
            result = builder.status(400).build();
        } else {
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                RestResponseBuilder builder = RestResponseBuilderFactory.produce();
                result = builder.status(500).build();
            } else {
                FileOutputStream fileOutput = null;
                try {
                    BufferedInputStream bufferedInput = new BufferedInputStream(inputStream, 8192);
                    fileOutput = new FileOutputStream(file);
                    try (BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput, 8192)) {
                        int read = bufferedInput.read();
                        while (read != -1) {
                            bufferedOutput.write(read);
                            read = bufferedInput.read();
                        }
                        bufferedOutput.flush();
                    }
                    fileOutput.flush();
                    fileOutput.close();
                } catch (IOException ioe) {
                    RestResponseBuilder builder = RestResponseBuilderFactory.produce();
                    result = builder.status(500).build();
                } finally {
                    try {
                        if (fileOutput != null) {
                            fileOutput.close();
                        }
                    } catch (IOException ioe) {
                        RestResponseBuilder builder = RestResponseBuilderFactory.produce();
                        result = builder.status(500).build();
                    }
                }
                if (result == null) {
                    RestResponseBuilder builder = RestResponseBuilderFactory.produce();
                    result = builder.status(200).build();
                }
            }
        }
        return result;
    }
}
