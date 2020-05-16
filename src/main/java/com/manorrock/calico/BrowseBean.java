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
import com.manorrock.calico.DirectoryModel;
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
import org.omnifaces.oyena.action.ActionMapping;

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
