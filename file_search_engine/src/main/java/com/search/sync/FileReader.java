package com.search.sync;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileReader {
    public String readContent(Path path) {
        try {
            return Files.readString(path);
        } catch (Exception e) {
            return null;
        }
    }
}
