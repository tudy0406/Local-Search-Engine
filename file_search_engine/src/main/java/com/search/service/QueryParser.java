package com.search.service;
public class QueryParser {

    public String parse(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        // basic normalization
        return input
                .trim()
                .toLowerCase()
                .replaceAll("\\s+", " AND ");
    }
}