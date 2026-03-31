package com.search.service;

import com.search.model.FileData;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ResultFormatter {

    public void printResults(List<FileData> results) {

        for (FileData file : results) {
            System.out.println("File: " + file.getPath());
            System.out.println("Size: " + file.getSize());

            long modifiedAt = file.getModifiedAt();
            Instant instant = Instant.ofEpochMilli(modifiedAt);
            String formatted = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault())
                    .format(instant);

            System.out.println("Last Modified: " + formatted);
            String preview = getPreview(file.getContent());
            System.out.println("Preview:\n" + preview);
            System.out.println("--------------------------------------------------");
        }
    }

    private String getPreview(String content) {
        if (content == null) return "";

        String[] lines = content.split("\n");
        StringBuilder preview = new StringBuilder();

        for (int i = 0; i < Math.min(3, lines.length); i++) {
            preview.append(lines[i]).append("\n");
        }

        return preview.toString();
    }
}