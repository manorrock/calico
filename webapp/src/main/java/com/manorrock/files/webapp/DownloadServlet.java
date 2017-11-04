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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

/**
 * The download servlet.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DownloadServlet extends HttpServlet {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(DownloadServlet.class.getName());

    /**
     * Stores the application.
     */
    @Inject
    private ApplicationBean application;
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param httpServletRequest servlet request
     * @param httpServletResponse servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, 
            HttpServletResponse httpServletResponse) throws ServletException, IOException {
        Client client = ClientBuilder.newBuilder().build();
        Response response = client.
                target(application.getFilesRestUrl()).
                path(httpServletRequest.getPathInfo()).
                request().
                get();
        httpServletResponse.setContentType("application/octet-stream");
        try (InputStream inputStream = response.readEntity(InputStream.class)) {
            BufferedInputStream bufferedInput = new BufferedInputStream(inputStream, 32768);
            try (OutputStream outputStream = new BufferedOutputStream(httpServletResponse.getOutputStream(), 32768)) {
                int inputByte = bufferedInput.read();
                while (inputByte != -1) {
                    outputStream.write(inputByte);
                    inputByte = bufferedInput.read();
                }
                outputStream.flush();
            }
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "Unable to download file", ioe);
            httpServletResponse.sendError(500);
        }
    }
}
