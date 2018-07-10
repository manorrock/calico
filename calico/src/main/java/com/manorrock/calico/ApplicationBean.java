/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
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

/**
 * The one and only application bean.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@ApplicationScoped
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
}
