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

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

/**
 * The bean used for deleting a file/directory.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Named("deleteFileBean")
@RequestScoped
public class DeleteFileBean {

    /**
     * Stores the application.
     */
    @Inject
    private ApplicationBean application;

    /**
     * Stores the directory path.
     */
    private String directoryPath;

    /**
     * Stores the name.
     */
    private String name;

    /**
     * Stores the HTTP servlet request.
     */
    @Inject
    private HttpServletRequest request;

    /**
     * Get the directory path.
     *
     * @return the directory path.
     */
    public String getDirectoryPath() {
        return directoryPath;
    }

    /**
     * Delete the file/directory.
     *
     * @return ""
     */
    public String delete() {
        Client client = ClientBuilder.newBuilder().build();
        Response response = client.target(application.getFilesRestUrl()).
                path(directoryPath).
                path(name).
                request().
                delete();
        if (response.getStatus() == 200) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Deieted " + name,
                            "Deleted " + name));
        }
        client.close();
        return "";
    }

    /**
     * Get the name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Initialize the bean.
     */
    @PostConstruct
    public void initialize() {
        name = request.getParameter("filename");
        directoryPath = request.getParameter("directoryPath");
    }
}
