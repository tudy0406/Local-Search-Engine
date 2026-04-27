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
            long lastAccessedAt = file.getLastAccessed();
            long lastSearched = file.getLastSearched();

            Instant modified_instant = Instant.ofEpochMilli(modifiedAt);
            Instant lastAccessed_instant = Instant.ofEpochMilli(lastAccessedAt);
            Instant lastSearched_instant = Instant.ofEpochMilli(lastSearched);
            String modified_formatted = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault())
                    .format(modified_instant);
            String accessed_formatted = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault())
                    .format(lastAccessed_instant);
            String searched_formatted = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault())
                    .format(lastSearched_instant);

            System.out.println("Last Modified: " + modified_formatted);
            System.out.println("Last Accessed: " + accessed_formatted);
            System.out.println("Last Searched: " + searched_formatted);
            String preview = getPreview(file.getContent());
            System.out.println("Preview:\n" + preview);
            System.out.println("--------------------------------------------------");
        }
    }

    private String getPreview(String content) {
        if (content == null || content.isEmpty()) return "";

        int maxLines = 3;
        int maxChars = 200;

        StringBuilder preview = new StringBuilder();
        int charCount = 0;
        int lineCount = 0;

        for (String line : content.split("\n")) {
            if (lineCount >= maxLines || charCount >= maxChars) break;

            int remainingChars = maxChars - charCount;

            if (line.length() <= remainingChars) {
                preview.append(line).append("\n");
                charCount += line.length() + 1; // +1 for newline
            } else {
                preview.append(line, 0, remainingChars);
                break;
            }

            lineCount++;
        }

        if (lineCount == 0) {
            return content.substring(0, Math.min(maxChars, content.length()));
        }

        return preview.toString();
    }
}