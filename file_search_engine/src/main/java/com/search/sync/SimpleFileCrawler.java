package com.search.sync;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class SimpleFileCrawler implements FileCrawler {

    @Override
    public List<Path> crawl(Path root, Set<String> excludedExtensions) {
        List<Path> result = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(root)) {

            paths.forEach(path -> {
                try {
                    if (Files.isRegularFile(path) && Files.isReadable(path)) {

                        String ext = getExtension(path);

                        if (!excludedExtensions.contains(ext)) {
                            result.add(path);
                        }

                    }
                } catch (Exception e) {
                    System.out.println("Skipping (error): " + path);
                }
            });

        } catch (IOException e) {
            throw new RuntimeException("Error while crawling root", e);
        }

        return result;
    }

    private String getExtension(Path path) {
        String name = path.getFileName().toString();
        int lastDot = name.lastIndexOf('.');
        if (lastDot == -1) return "";
        return name.substring(lastDot).toLowerCase(); // ".txt"
    }
}