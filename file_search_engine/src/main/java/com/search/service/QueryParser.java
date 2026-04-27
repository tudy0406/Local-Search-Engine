package com.search.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueryParser {

    private static final List<String> QUALIFIERS = List.of("path", "content");
    private static final String BARE = "";
    private static final Pattern QUALIFIED_TOKEN = Pattern.compile(
            "^(" + String.join("|", QUALIFIERS) + ")\\s*:\\s*(.+)$",
            Pattern.CASE_INSENSITIVE);

    public String parse(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        // LinkedHashMap preserves first-seen order for deterministic output.
        Map<String, List<String>> groups = new LinkedHashMap<>();

        for (String token : input.trim().split("\\s+")) {
            Matcher m = QUALIFIED_TOKEN.matcher(token);
            String qualifier;
            String value;
            if (m.matches()) {
                qualifier = m.group(1).toLowerCase(Locale.ROOT);
                value     = m.group(2);
            } else {
                qualifier = BARE;
                value     = token;
            }

            for (String term : sanitiseTerms(value)) {
                groups.computeIfAbsent(qualifier, k -> new ArrayList<>()).add(term);
            }
        }

        return groups.entrySet().stream()
                .map(e -> renderGroup(e.getKey(), e.getValue()))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(" AND "));
    }

    private static List<String> sanitiseTerms(String value) {
        if (value == null || value.isBlank()) return List.of();
        return Stream.of(value.toLowerCase(Locale.ROOT).split("[^a-z0-9]+"))
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());
    }

    private static String renderGroup(String qualifier, List<String> terms) {
        if (terms.isEmpty()) return "";
        String prefix = qualifier.isEmpty() ? "" : qualifier + ":";
        if (terms.size() == 1) return prefix + terms.get(0);
        String joined = terms.stream()
                .map(t -> prefix + t)
                .collect(Collectors.joining(" AND "));
        return "(" + joined + ")";
    }
}
