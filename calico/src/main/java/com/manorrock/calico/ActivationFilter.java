/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
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
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * The activation filter for the UI.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ActivationFilter implements Filter {

    /**
     * Stores the enable UI flag.
     */
    private boolean enableUI = true;
    
    /**
     * Stores the filter configuration.
     */
    private FilterConfig filterConfig;

    /**
     * Constructor.
     */
    public ActivationFilter() {
        if (System.getenv("ENABLE_UI") != null) {
            enableUI = Boolean.getBoolean(System.getProperty("ENABLE_UI"));
        } else if (System.getProperty("ENABLE_UI") != null) {
            enableUI = Boolean.getBoolean(System.getProperty("ENABLE_UI"));
        }        
    }

    /**
     * Destroy the filter.
     */
    @Override
    public void destroy() {
    }

    /**
     * Execute the filter.
     *
     * @param request the Servlet request.
     * @param response the Servlet response.
     * @param chain the Filter chain.
     * @exception IOException when an I/O error occurs.
     * @exception ServletException when a Servlet error occurs.
     */
    @Override
    public void doFilter(
            ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (enableUI) {
            chain.doFilter(request, response);
        } else {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Get the filter configuration.
     *
     * @return the filter configuration.
     */
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    /**
     * Initialize the filter.
     * 
     * @param filterConfig the filter configuration.
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }
}
