/*
 * Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.calico;

import java.io.IOException;
import java.io.InputStream;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The File resource.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Path("")
@RequestScoped
public class FileResource {

    /**
     * Stores the application.
     */
    @Inject
    private ApplicationBean application;

    /**
     * Create a file.
     *
     * @param path the path.
     * @param inputStream the input stream.
     * @return the response.
     */
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Path("{path:.+}")
    @POST
    public Response createFile(@PathParam("path") String path, InputStream inputStream) {
        Response result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        if (application.existingFile(path)) {
            result = Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            try {
                application.createFile(path, inputStream);
                result = Response.status(201).header("Location", path).build();
            } catch (IOException ioe) {
            }
        }
        return result;
    }

    /**
     * Delete a file.
     *
     * @param path the path.
     * @return the response.
     */
    @DELETE
    @Path("{path:.+}")
    public Response deleteFile(@PathParam("path") String path) {
        Response result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        if (!application.existingFile(path)) {
            result = Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            application.deleteFile(path);
        }
        return result;
    }

    /**
     * Get a file.
     *
     * @param path the path.
     * @return the response.
     */
    @GET
    @Path("{path:.+}")
    public Response getFile(@PathParam("path") String path) {
        Response result;
        if (!application.existingFile(path)) {
            result = Response.status(404).build();
        } else if (application.isDirectory(path)) {
            result = Response.ok(application.getDirectory(path), MediaType.APPLICATION_JSON_TYPE).build();
        } else {
            result = Response.ok(application.getInputStream(path), MediaType.APPLICATION_OCTET_STREAM_TYPE).build();
        }
        return result;
    }
    
    /**
     * Get the root directory.
     * 
     * @return the root directory.
     */
    @GET
    public Response getRootDirectory() {
        return getFile("");
    }
    
     /**
     * Update a file.
     *
     * @param path the path.
     * @param inputStream the input stream.
     * @return the response.
     */
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Path("{path:.+}")
    @PUT
    public Response updateFile(@PathParam("path") String path, InputStream inputStream) {
        Response result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        if (!application.existingFile(path)) {
            result = Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            try {
                application.updateFile(path, inputStream);
                result = Response.ok().build();
            } catch (IOException ioe) {
            }
        }
        return result;
    }
}
