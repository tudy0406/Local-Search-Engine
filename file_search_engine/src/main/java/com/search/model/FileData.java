package com.search.model;

import java.nio.file.Path;
import java.sql.Date;

public class FileData {
    private Path path;
    private String content;
    private long size;
    private long modifiedAt;
    private String extension;

    public FileData(Path path,  String content, long size, long modifiedAt, String extension) {
        this.path = path;
        this.content = content;
        this.size = size;
        this.modifiedAt = modifiedAt;
        this.extension = extension;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
