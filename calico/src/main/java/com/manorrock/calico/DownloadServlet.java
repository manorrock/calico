/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.calico;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The "Download" servlet.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@WebServlet(urlPatterns = "/download/*")
public class DownloadServlet extends HttpServlet {

    /**
     * Stores the application.
     */
    @Inject
    private ApplicationBean application;
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request the request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request,  
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/octet-stream");
        try (InputStream inputStream = application.getInputStream(request.getPathInfo())) {
            BufferedInputStream bufferedInput = new BufferedInputStream(inputStream, 32768);
            try (OutputStream outputStream = new BufferedOutputStream(response.getOutputStream(), 32768)) {
                int inputByte = bufferedInput.read();
                while (inputByte != -1) {
                    outputStream.write(inputByte);
                    inputByte = bufferedInput.read();
                }
                outputStream.flush();
            }
        } catch (IOException ioe) {
            response.sendError(500);
        }
    }
}
