package com.search.sync;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;

public class MetadataExtractor {
    public long getSize(Path path) throws IOException {
        return Files.size(path);
    }

    public long getModifiedTime(Path path) throws IOException {
        return Files.getLastModifiedTime(path).toMillis();
    }

    public String getExtension(Path path) {
        String name = path.getFileName().toString();
        int i = name.lastIndexOf('.');
        return i == -1 ? "" : name.substring(i).toLowerCase();
    }
}
