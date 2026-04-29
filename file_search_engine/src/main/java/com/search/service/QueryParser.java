package com.search.service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QueryParser {

    private static final List<String> QUALIFIERS = List.of("path", "content");
    private static final String BARE = "";

    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "(?:(path|content)\\s*:\\s*)?\"([^\"]+)\"|(?:(path|content)\\s*:\\s*)?(\\S+)",
            Pattern.CASE_INSENSITIVE
    );

    public String parse(String input) {
        if (input == null || input.isBlank()) return "";

        Map<String, List<String>> groups = new LinkedHashMap<>();

        Matcher matcher = TOKEN_PATTERN.matcher(input);

        while (matcher.find()) {
            String qualifier = null;
            String value = null;

            // Case 1: quoted phrase with qualifier
            if (matcher.group(1) != null && matcher.group(2) != null) {
                qualifier = matcher.group(1);
                value = matcher.group(2);
            }
            // Case 2: unquoted with qualifier
            else if (matcher.group(3) != null && matcher.group(4) != null) {
                qualifier = matcher.group(3);
                value = matcher.group(4);
            }
            // Case 3: bare phrase
            else if (matcher.group(2) != null) {
                qualifier = BARE;
                value = matcher.group(2);
            }
            // Case 4: bare word
            else if (matcher.group(4) != null) {
                qualifier = BARE;
                value = matcher.group(4);
            }

            if (value == null) continue;

            qualifier = qualifier == null ? BARE : qualifier.toLowerCase(Locale.ROOT);

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

        return Arrays.stream(value.toLowerCase(Locale.ROOT).split("[^a-z0-9]+"))
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