/*
 *  Copyright (c) 2002-2016, Manorrock.com. All Rights Reserved.
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

import java.io.Serializable;

/**
 * A file attribute model.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class FileAttributesModel implements Serializable {

    /**
     * Stores the created.
     */
    private long created;

    /**
     * Stores the directory flag.
     */
    private boolean directory;

    /**
     * Stores the last accessed.
     */
    private long lastAccessed;

    /**
     * Stores the last modified.
     */
    private long lastModified;

    /**
     * Stores the length.
     */
    private long length;

    /**
     * Stores the symbolic link flag.
     */
    private boolean symbolicLink;

    /**
     * Get the created.
     *
     * @return the created.
     */
    public long getCreated() {
        return created;
    }

    /**
     * Get the last accessed.
     *
     * @return the last accessed.
     */
    public long getLastAccessed() {
        return lastAccessed;
    }

    /**
     * Get the last modified.
     *
     * @return the last modified.
     */
    public long getLastModified() {
        return lastModified;
    }

    /**
     * Get the length.
     *
     * @return the length.
     */
    public long getLength() {
        return length;
    }

    /**
     * Is directory.
     *
     * @return true if a directory, false otherwise.
     */
    public boolean isDirectory() {
        return directory;
    }

    /**
     * Is the file a symbolic link.
     *
     * @return true if it is a symbolic link, false otherwise.
     */
    public boolean isSymbolicLink() {
        return symbolicLink;
    }

    /**
     * Set the created.
     *
     * @param created the created.
     */
    public void setCreated(long created) {
        this.created = created;
    }

    /**
     * Set the directory flag.
     *
     * @param directory the directory flag.
     */
    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    /**
     * Set the last accessed.
     *
     * @param lastAccessed the last accessed.
     */
    public void setLastAccessed(long lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    /**
     * Set the last modified.
     *
     * @param lastModified the last modified.
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Set the length.
     *
     * @param length the length.
     */
    public void setLength(long length) {
        this.length = length;
    }

    /**
     * Set the symbolic link flag.
     *
     * @param symbolicLink the symbolic link flag.
     */
    public void setSymbolicLink(boolean symbolicLink) {
        this.symbolicLink = symbolicLink;
    }
}
