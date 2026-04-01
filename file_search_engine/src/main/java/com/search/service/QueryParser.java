package com.search.service;
public class QueryParser {

    public String parse(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        // remove punctuation
        String cleaned = input
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s]", " "); // remove symbols like : , . !

        // normalize spaces + AND logic
        return cleaned.trim().replaceAll("\\s+", " AND ");
    }
}