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
package com.manorrock.files.webapp;

import com.manorrock.files.model.DirectoryModel;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXB;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;

/**
 * The bean used for browsing the hierarchy.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Named("browseBean")
@RequestScoped
public class BrowseBean implements Serializable {

    /**
     * Stores the application.
     */
    @Inject
    private ApplicationBean application;

    /**
     * Stores the directory.
     */
    private DirectoryModel directory;
    /**
     * Stores the directory path;
     */
    private String directoryPath = "";

    /**
     * Stores the file (part).
     */
    private Part file;
    /**
     * Create directory flow.
     *
     * @return "create_directory".
     */
    public String createDirectory() {
        return "create_directory";
    }

    /**
     * Delete file flow.
     *
     * @return "delete_file".
     */
    public String deleteFile() {
        return "delete_file";
    }

    /**
     * Initialize the bean.
     */
    @PostConstruct
    public void initialize() {
        directoryPath = FacesContext.getCurrentInstance().getExternalContext().
                getRequestParameterMap().get("directory");
        
        if (directoryPath != null) {
            while(directoryPath.endsWith("/")) {
                directoryPath = directoryPath.substring(0, directoryPath.length() - 1);
            }
            
            Client client = ClientBuilder.newBuilder().register(MoxyJsonFeature.class).build();
            try {
                directory = client.
                        target(application.getFilesRestUrl()).
                        path(directoryPath).
                        request().
                        get(DirectoryModel.class);

            } catch (NotFoundException nfe) {
                directory = null;
            }
            client.close();
        }
    }

    /**
     * Get the directory.
     *
     * @return the directory.
     */
    public DirectoryModel getDirectory() {
        return directory;
    }

    /**
     * Get the directory path.
     *
     * @return the directory path.
     */
    public String getDirectoryPath() {
        return directoryPath;
    }

    /**
     * Get the file (part).
     *
     * @return the file (part).
     */
    public Part getFile() {
        return file;
    }

    /**
     * Set the directory path.
     *
     * @param directoryPath the directory path.
     */
    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    /**
     * Set the file (part).
     *
     * @param file the file (part)
     */
    public void setFile(Part file) {
        this.file = file;
    }

    /**
     * Upload.
     *
     * @return ""
     */
    public Object upload() {
        Client client = ClientBuilder.newBuilder().build();
        try {
            String fileName = file.getSubmittedFileName();
            Entity entity = Entity.entity(file.getInputStream(),
                    MediaType.APPLICATION_OCTET_STREAM);
            client.target(application.getFilesRestUrl()).
                    path(directoryPath).
                    path(fileName).
                    request().
                    post(entity);
        } catch (IOException ioe) {
        }
        Response response = client.
                target(application.getFilesRestUrl()).
                path(directoryPath).
                request().
                get();
        String responseText = (String) response.readEntity(String.class);
        directory = JAXB.unmarshal(new StringReader(responseText), DirectoryModel.class);
        client.close();
        return "";
    }
}
