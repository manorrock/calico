/*
 *  Copyright (c) 2002-2023, Manorrock.com. All Rights Reserved.
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

import java.io.IOException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * The "Create a directory" bean.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Named("createDirectoryBean")
@RequestScoped
public class CreateDirectoryBean {
    
    /**
     * Stores the application.
     */
    @Inject
    private ApplicationBean application;

    /**
     * Stores the directory path.
     */
    private String directoryPath = "";

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
     * Create the directory.
     *
     * @return ""
     */
    public String create() {
        application.createDirectory(directoryPath, name);
        StringBuilder redirectUrl = new StringBuilder();
        redirectUrl.append(externalContext.getRequestScheme());
        redirectUrl.append("://");
        redirectUrl.append(externalContext.getRequestServerName());
        redirectUrl.append(externalContext.getRequestServerPort() == 80 ? "" : ":" + externalContext.getRequestServerPort());
        redirectUrl.append(externalContext.getRequestContextPath());
        redirectUrl.append("/directory/view/");
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
     * Set the name.
     *
     * @param name the name.
     */
    public void setName(String name) {
        this.name = name;
    }
}
