/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.calico;

import com.manorrock.oyena.action.ActionMapping;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

/**
 * The browse bean.
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
     * Stores the directory path.
     */
    private String directoryPath;

    /**
     * Browse the directory hierarchy.
     *
     * @param request the request.
     * @param facesContext the Faces context.
     * @return "/browse.xhtml"
     */
    @ActionMapping("/*")
    public String browse(HttpServletRequest request, FacesContext facesContext) {
        if (request.getMethod().equalsIgnoreCase("post")) {
            try {
                Part file = request.getPart("file");
                String filename = file.getSubmittedFileName();
                InputStream inputStream = file.getInputStream();
                directoryPath = request.getParameter("directoryPath");
                application.createFile(directoryPath, filename, inputStream);
                directory = application.getDirectory(directoryPath);
            } catch (IOException | ServletException e) {
                throw new FacesException(e);
            }
        } else {
            directoryPath = request.getPathInfo().substring(1);
            directory = application.getDirectory(directoryPath);
        }
        return "/browse.xhtml";
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
     * Get the parent path.
     *
     * @return the parent path.
     */
    public String getParentPath() {
        String result;
        if (directoryPath.endsWith("/")) {
            result = directoryPath.substring(0, directoryPath.length());
        } else {
            result = directoryPath;
        }
        if (result.contains("/")) {
            result = result.substring(0, result.lastIndexOf("/"));
        } else {
            result = "";
        }
        return result;
    }
}
