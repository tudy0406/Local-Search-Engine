package com.search.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ConfigLoader {

    private final Properties props = new Properties();

    public ConfigLoader(String path) {
        try (FileInputStream fis = new FileInputStream(path)) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public String getRootDir() {
        return props.getProperty("root.dir");
    }

    public Set<String> getIgnoredExtensions() {
        String value = props.getProperty("ignore.extensions", "");
        return parseToSet(value);
    }

    public Set<String> getIgnoredDirectories() {
        String value = props.getProperty("ignore.directories", "");
        return parseToSet(value);
    }

    private Set<String> parseToSet(String value) {
        Set<String> result = new HashSet<>();

        if (value == null || value.isBlank()) return result;

        String[] parts = value.split(",");

        for (String p : parts) {
            String trimmed = p.trim().toLowerCase();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }

        return result;
    }
}