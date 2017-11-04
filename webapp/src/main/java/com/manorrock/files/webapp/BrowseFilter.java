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

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * The browse filter.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@WebFilter(filterName = "BrowseFilter", urlPatterns = {"/*"})
public class BrowseFilter implements Filter {

    /**
     * Destroy the filter.
     */
    @Override
    public void destroy() {
    }

    /**
     * Process the filter request.
     *
     * @param request the Servlet request.
     * @param response the Servlet response.
     * @param chain the Filter chain.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            if (httpServletRequest.getServletPath().startsWith("/_/")
                    || httpServletRequest.getServletPath().startsWith("/download")
                    || httpServletRequest.getServletPath().startsWith("/javax.faces.resource")
                    || httpServletRequest.getServletPath().startsWith("/style.css")) {
                chain.doFilter(httpServletRequest, response);
            } else {
                String directory = httpServletRequest.getServletPath();
                if (directory.endsWith("/index.xhtml")) {
                    directory = directory.substring(0, directory.indexOf("/index.xhtml"));
                }
                if (directory.startsWith("/")) {
                    directory = directory.substring(1);
                }
                RequestDispatcher requestDispatcher = httpServletRequest.getRequestDispatcher(
                        "/_/browse.xhtml?directory=" + directory);
                requestDispatcher.forward(httpServletRequest, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * Initialize the filter.
     *
     * @param filterConfig the Filter configuration.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
