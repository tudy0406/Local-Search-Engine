package com.search.sync;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Stream;

public class SimpleFileCrawler implements FileCrawler {

    @Override
    public List<Path> crawl(Path root,
                            Set<String> excludedExtensions,
                            Set<String> excludedDirectories) {

        List<Path> result = new ArrayList<>();

        try {
            Files.walkFileTree(root, new SimpleFileVisitor<>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {

                    String dirName = dir.getFileName().toString().toLowerCase();

                    if (excludedDirectories.contains(dirName)) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

                    try {
                        if (Files.isReadable(file)) {

                            String ext = getExtension(file);

                            if (!excludedExtensions.contains(ext)) {
                                result.add(file);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Skipping (error): " + file);
                    }

                    return FileVisitResult.CONTINUE;
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