/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.calico;

import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

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
     * Set the name.
     *
     * @param name the name.
     */
    public void setName(String name) {
        this.name = name;
    }
}
