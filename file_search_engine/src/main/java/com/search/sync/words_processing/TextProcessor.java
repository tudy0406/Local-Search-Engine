package com.search.sync.words_processing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextProcessor {

    public static List<String> tokenize(String text) {
        return Arrays.stream(text.toLowerCase().split("[^a-z0-9]+"))
                .filter(s -> !s.isEmpty())
                .toList();
    }

    public static Map<String, Integer> countWords(String text) {
        Map<String, Integer> map = new HashMap<>();

        for (String word : tokenize(text)) {
            map.put(word, map.getOrDefault(word, 0) + 1);
        }

        return map;
    }
}
