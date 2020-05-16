/*
 *  Copyright (c) 2002-2019, Manorrock.com. All Rights Reserved.
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

import com.manorrock.calico.ApplicationBean;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * The "Delete a file/directory" bean.
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
     * Stores the External context.
     */
    @Inject
    private ExternalContext externalContext;

    /**
     * Stores the Faces context.
     */
    @Inject
    private FacesContext facesContext;

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
     * Delete the file/directory.
     *
     * @return ""
     */
    public String delete() {
        application.delete(directoryPath, name);
        StringBuilder redirectUrl = new StringBuilder();
        redirectUrl.append(externalContext.getRequestScheme());
        redirectUrl.append("://");
        redirectUrl.append(externalContext.getRequestServerName());
        redirectUrl.append(externalContext.getRequestServerPort() == 80 ? "" : ":" + externalContext.getRequestServerPort());
        redirectUrl.append(externalContext.getRequestContextPath());
        redirectUrl.append("/browse/");
        redirectUrl.append(directoryPath);
        try {
            facesContext.getExternalContext().redirect(redirectUrl.toString());
        } catch (IOException ioe) {
        }
        facesContext.responseComplete();
        return "";
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
