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
package com.manorrock.files.rest;

import com.manorrock.httpserver.HttpServer;
import com.manorrock.httpserver.HttpServerBuilder;
import com.manorrock.httpserver.HttpServerBuilderFactory;
import com.manorrock.rest.DefaultBodyParamProducer;
import com.manorrock.rest.DefaultInstanceProducer;
import com.manorrock.rest.Route;
import com.manorrock.rest.RouteBuilder;
import com.manorrock.rest.RouteBuilderFactory;
import com.manorrock.rest.httpserver.HttpServerRestProcessor;
import com.manorrock.rest.httpserver.HttpServerRestRuntime;
import com.manorrock.rest.json.JsonBodyWriter;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * The File REST Main.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class FileMain {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(FileMain.class.getName());

    /**
     * Stores the HTTP server REST runtime.
     */
    private final HttpServerRestRuntime runtime = new HttpServerRestRuntime();

    /**
     * Run method.
     */
    public void run() {
        LOGGER.info("Starting up");
        String pathPrefix = System.getenv("PATH_PREFIX");
        if (pathPrefix == null) {
            pathPrefix = System.getProperty("PATH_PREFIX", "/files/rest");
        }
        RouteBuilder routeBuilder = RouteBuilderFactory.produce();
        FileResource fileResource = new FileResource();
        Route getFileRoute = routeBuilder
                .httpMethod("GET")
                .path(pathPrefix + "(?<path>.*)")
                .pathParam("path")
                .instanceProducer(new DefaultInstanceProducer(fileResource))
                .biFunction((t, u) -> {
                    FileResource resource = (FileResource) t;
                    Object[] parameters = (Object[]) u;
                    String path = (String) parameters[0];
                    return resource.getFile(path);
                })
                .build();
        runtime.getRouter().addRoute(getFileRoute);
        routeBuilder = RouteBuilderFactory.produce();
        Route deleteFileRoute = routeBuilder
                .httpMethod("DELETE")
                .path(pathPrefix + "(?<path>.*)")
                .pathParam("path")
                .instanceProducer(new DefaultInstanceProducer(fileResource))
                .biFunction((t, u) -> {
                    FileResource resource = (FileResource) t;
                    Object[] parameters = (Object[]) u;
                    String path = (String) parameters[0];
                    return resource.deleteFile(path);
                })
                .build();
        runtime.getRouter().addRoute(deleteFileRoute);
        routeBuilder = RouteBuilderFactory.produce();
        Route createFileRoute = routeBuilder
                .httpMethod("POST")
                .path(pathPrefix + "(?<path>.*)")
                .pathParam("path")
                .queryParam("directory")
                .instanceProducer(new DefaultInstanceProducer(fileResource))
                .biFunction((t, u) -> {
                    FileResource resource = (FileResource) t;
                    Object[] parameters = (Object[]) u;
                    String path = (String) parameters[0];
                    Boolean directory = false;
                    if (parameters[1] != null) {
                        directory = Boolean.valueOf((String) parameters[1]);
                    }
                    InputStream inputStream = (InputStream) parameters[2];
                    return resource.createFile(path, directory, inputStream);
                })
                .build();
        createFileRoute.getParamProducers().add(new DefaultBodyParamProducer(InputStream.class));
        runtime.getRouter().addRoute(createFileRoute);
        routeBuilder = RouteBuilderFactory.produce();
        Route getFileAttributesRoute = routeBuilder
                .httpMethod("OPTIONS")
                .path(pathPrefix + "(?<path>.*)")
                .pathParam("path")
                .instanceProducer(new DefaultInstanceProducer(fileResource))
                .biFunction((t, u) -> {
                    FileResource resource = (FileResource) t;
                    Object[] parameters = (Object[]) u;
                    String path = (String) parameters[0];
                    return resource.getFileAttributes(path);
                })
                .build();
        runtime.getRouter().addRoute(getFileAttributesRoute);
        routeBuilder = RouteBuilderFactory.produce();
        Route updateFileRoute = routeBuilder
                .httpMethod("PUT")
                .path(pathPrefix + "(?<path>.*)")
                .pathParam("path")
                .instanceProducer(new DefaultInstanceProducer(fileResource))
                .biFunction((t, u) -> {
                    FileResource resource = (FileResource) t;
                    Object[] parameters = (Object[]) u;
                    String path = (String) parameters[0];
                    InputStream inputStream = (InputStream) parameters[1];
                    return resource.updateFile(path, inputStream);
                })
                .build();
        updateFileRoute.getParamProducers().add(new DefaultBodyParamProducer(InputStream.class));
        runtime.getRouter().addRoute(updateFileRoute);
        runtime.getRouter().addBodyWriter("application/json", new JsonBodyWriter());
        int portNumber = 8081;
        if (System.getenv("SERVER_PORT") != null) {
            portNumber = Integer.valueOf(System.getenv("SERVER_PORT"));
        }
        if (portNumber == 8081 && System.getProperty("SERVER_PORT") != null) {
            portNumber = Integer.valueOf(System.getProperty("SERVER_PORT"));
        }
        HttpServerBuilder builder = HttpServerBuilderFactory.produce().
                port(portNumber).
                processor(new HttpServerRestProcessor(runtime));
        HttpServer server = builder.build();
        server.start();
        LOGGER.info("Startup completed");
        while (server.isRunning()) {
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
