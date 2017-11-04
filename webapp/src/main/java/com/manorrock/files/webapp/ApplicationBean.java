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

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * The one and only application bean.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@ApplicationScoped
public class ApplicationBean {

    /**
     * Stores the files REST URL.
     */
    private String filesRestUrl;

    /**
     * Get the Files REST URL.
     *
     * @return the Files REST URL.
     */
    public String getFilesRestUrl() {
        return filesRestUrl;
    }

    /**
     * Initialize.
     */
    @PostConstruct
    public void initialize() {
        if (filesRestUrl == null) {
            try {
                InitialContext initialContext = new InitialContext();
                filesRestUrl = (String) initialContext.lookup("java:comp/env/filesRestUrl");
            } catch (NamingException ne) {
            }
        }

        if (filesRestUrl == null || filesRestUrl.trim().equals("")) {
            filesRestUrl = System.getenv("FILES_REST_URL");
        }

        if (filesRestUrl == null || filesRestUrl.trim().equals("")) {
            filesRestUrl = System.getProperty("FILES_REST_URL", 
                    "http://localhost:8081/files/rest");
        }
    }
}
