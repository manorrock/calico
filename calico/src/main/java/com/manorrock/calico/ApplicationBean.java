/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
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
package com.manorrock.calico;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

/**
 * The one and only application bean.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@ApplicationScoped
@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/login.xhtml",
                errorPage = ""
        )
)
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "jdbc/calico",
        callerQuery = "select password from user_account where username = ?",
        groupsQuery = "select role_name from user_role where username = ?",
        hashAlgorithm = Pbkdf2PasswordHash.class,
        hashAlgorithmParameters = {
            "Pbkdf2PasswordHash.Iterations=3072",
            "Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512",
            "Pbkdf2PasswordHash.SaltSizeBytes=64"
        }
)
public class ApplicationBean implements Serializable {

    /**
     * Stores the root directory.
     */
    private File rootDirectory;

    /**
     * Create the directory.
     *
     * @param directoryPath the directory path.
     * @param name the name.
     */
    public void createDirectory(String directoryPath, String name) {
        if (!name.trim().equals("")) {
            File baseDirectory = new File(rootDirectory, directoryPath);
            File newDirectory = new File(baseDirectory, name);
            if (!newDirectory.exists()) {
                newDirectory.mkdirs();
            }
        }
    }

    /**
     * Create the file.
     *
     * @param directoryPath the directory path.
     * @param filename the filename.
     * @param inputStream the input stream.
     * @throws IOException when an I/O error occurs.
     */
    public void createFile(String directoryPath, String filename, InputStream inputStream) throws IOException {
        try (BufferedInputStream bufferedInput = new BufferedInputStream(inputStream, 8192)) {
            File baseDirectory = new File(rootDirectory, directoryPath);
            try (FileOutputStream fileOutput = new FileOutputStream(new File(baseDirectory, filename))) {
                try (BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput, 8192)) {
                    byte[] buffer = new byte[8192];
                    while (bufferedInput.read(buffer) > 0) {
                        bufferedOutput.write(buffer);
                    }
                    bufferedOutput.flush();
                }
                fileOutput.flush();
            }
        }
    }

    /**
     * Create the file.
     *
     * @param filePath the file path.
     * @param inputStream the input stream.
     * @throws IOException when an I/O error occurs.
     */
    public void createFile(String filePath, InputStream inputStream) throws IOException {
        try (BufferedInputStream bufferedInput = new BufferedInputStream(inputStream, 8192)) {
            try (FileOutputStream fileOutput = new FileOutputStream(new File(rootDirectory, filePath))) {
                try (BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput, 8192)) {
                    byte[] buffer = new byte[8192];
                    while (bufferedInput.read(buffer) > 0) {
                        bufferedOutput.write(buffer);
                    }
                    bufferedOutput.flush();
                }
                fileOutput.flush();
            }
        }
    }

    /**
     * Delete the directory.
     *
     * @param directoryPath the directory path.
     * @param name the name.
     */
    public void delete(String directoryPath, String name) {
        File baseDirectory = new File(rootDirectory, directoryPath);
        File file = new File(baseDirectory, name);
        deleteFile(file);
    }

    /**
     * Delete the file.
     *
     * @param filePath the file path.
     */
    public void deleteFile(String filePath) {
        File file = new File(rootDirectory, filePath);
        deleteFile(file);
    }

    /**
     * Delete the file.
     *
     * @param file the file.
     */
    private void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File toDelete : files) {
                    deleteFile(toDelete);
                }
            }
        }
        file.delete();
    }

    /**
     * Is this an existing file?
     *
     * @param filePath the file path.
     * @return true if the file exists, false otherwise.
     */
    public boolean existingFile(String filePath) {
        File file = new File(rootDirectory, filePath);
        return file.exists();
    }

    /**
     * Initialize the bean.
     */
    @PostConstruct
    public void initialize() {
        String rootDirectoryFilename = System.getenv("ROOT_DIRECTORY");
        if (rootDirectoryFilename == null) {
            rootDirectoryFilename = System.getProperty("ROOT_DIRECTORY",
                    System.getProperty("user.home") + "/.manorrock/calico");
        }
        rootDirectory = new File(rootDirectoryFilename);
        if (!rootDirectory.exists()) {
            rootDirectory.mkdirs();
        }
    }

    /**
     * Get the directory model for the given directory path.
     *
     * @param directoryPath the directory path.
     * @return the directory model, or null if not found.
     */
    public DirectoryModel getDirectory(String directoryPath) {
        DirectoryModel result = new DirectoryModel();
        try {
            if ("/".equals(directoryPath)) {
                directoryPath = "";
            }
            File file = new File(rootDirectory, directoryPath);
            if (file.equals(rootDirectory)) {
                result.setName("");
            } else {
                result.setName(directoryPath);
            }
            ArrayList<FileModel> files = new ArrayList<>();
            File[] fileList = file.listFiles();
            Arrays.sort(fileList);
            for (File currentFile : fileList) {
                String name = currentFile.getAbsolutePath().substring(file.getCanonicalPath().length() + 1);
                FileAttributesModel attributes = (FileAttributesModel) getFileAttributes(directoryPath + "/" + name);
                name = name.replaceAll("&", "&amp;");
                boolean show = true;
                if (System.getProperty("os.name").toLowerCase().contains("mac os") && name.startsWith(".")) {
                    show = false;
                }
                if (show) {
                    FileModel fileModel = new FileModel();
                    fileModel.setName(name);
                    fileModel.setAttributes(attributes);
                    files.add(fileModel);
                }
            }
            result.setFiles(files);
        } catch (IOException ioe) {
            result = null;
        }
        return result;
    }

    /**
     * Get the file attributes.
     *
     * @param path the path
     * @return the response.
     */
    private FileAttributesModel getFileAttributes(String path) {
        FileAttributesModel result;
        File file = new File(rootDirectory, path);
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
            result = null;
        }
        return result;
    }

    /**
     * Get the input stream.
     *
     * @param path the path.
     * @return the input stream, or null if not found.
     */
    public InputStream getInputStream(String path) {
        InputStream result = null;
        File file = new File(rootDirectory, path);
        if (file.exists()) {
            try {
                result = new BufferedInputStream(new FileInputStream(file));
            } catch (FileNotFoundException fnfe) {
                result = null;
            }
        }
        return result;
    }

    /**
     * Is the given path a directory?
     *
     * @param directoryPath the directory path.
     * @return true if it is, false otherwise.
     */
    public boolean isDirectory(String directoryPath) {
        File directory = new File(rootDirectory, directoryPath);
        return directory.isDirectory();
    }

    /**
     * Update a file.
     *
     * @param filePath the file path.
     * @param inputStream the input stream.
     * @throws IOException when an I/O error occurs.
     */
    public void updateFile(String filePath, InputStream inputStream) throws IOException {
        try (BufferedInputStream bufferedInput = new BufferedInputStream(inputStream, 8192)) {
            try (FileOutputStream fileOutput = new FileOutputStream(new File(rootDirectory, filePath))) {
                try (BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput, 8192)) {
                    byte[] buffer = new byte[8192];
                    while (bufferedInput.read(buffer) > 0) {
                        bufferedOutput.write(buffer);
                    }
                    bufferedOutput.flush();
                }
                fileOutput.flush();
            }
        }
    }
}
