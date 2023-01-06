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
import java.io.InputStream;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
