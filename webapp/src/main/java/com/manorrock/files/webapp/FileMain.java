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

import com.manorrock.httpserver.HttpServer;
import com.manorrock.httpserver.HttpServerBuilderFactory;
import com.manorrock.webapp.DefaultResourceManager;
import com.manorrock.webapp.DefaultWebApplicationClassLoader;
import com.manorrock.webapp.DefaultWebApplicationServer;
import com.manorrock.webapp.WebApplication;
import com.manorrock.webapp.WebApplicationBuilder;
import com.manorrock.webapp.WebApplicationBuilderFactory;
import com.manorrock.webapp.weld.WeldObjectInstanceManager;
import java.util.logging.Logger;

/**
 * The main method.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class FileMain {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(FileMain.class.getName());

    /**
     * Run method.
     */
    public void run() {
        System.getProperties().put("java.naming.factory.initial", "com.manorrock.jndi.DefaultInitialContextFactory");
        DefaultWebApplicationServer server = new DefaultWebApplicationServer();
        HttpServer httpServer = HttpServerBuilderFactory.produce().
                port(8082).
                processor(server).
                build();
        WebApplicationBuilder webAppBuilder = WebApplicationBuilderFactory.produce();
        WebApplication webApp = webAppBuilder.contextPath("/files/webapp").build();
        DefaultResourceManager resourceManager = new DefaultResourceManager();
        webApp.setObjectInstanceManager(new WeldObjectInstanceManager());
        webApp.setClassLoader(new DefaultWebApplicationClassLoader(resourceManager));
        webApp.addFilterMapping("Browse Filter", "/*");
        webApp.addFilter("Browse Filter", "com.manorrock.files.webapp.BrowseFilter");
        webApp.addServletMapping("Faces Servlet", "*.xhtml");
        webApp.addServletMapping("Faces Servlet", "/faces/*");
        webApp.addServlet("Faces Servlet", "javax.faces.webapp.FacesServlet");
        webApp.addServletMapping("Download Servlet", "/download/*");
        webApp.addServlet("Download Servlet", "com.manorrock.files.webapp.DownloadServlet");
        webApp.addListener("org.jboss.weld.environment.servlet.Listener");
        webApp.addListener("com.sun.faces.config.ConfigureListener");
        server.addWebApplication(webApp);
        server.initialize();
        server.start();
        httpServer.start();
        LOGGER.info("Startup completed");
        while (httpServer.isRunning()) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ie) {
            }
        }
    }

    /**
     * Main method.
     *
     * @param arguments the arguments.
     */
    public static void main(String[] arguments) {
        FileMain main = new FileMain();
        main.run();
    }
}
