package com.search.service.spelling_correction;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SpellCorrector {
    private final Map<String, Integer> wordFreq;

    public SpellCorrector(Map<String, Integer> wordFreq) {
        this.wordFreq = wordFreq;
    }

    public String correctQuery(String query) {
        String[] tokens = query.split("\\s+");
        StringBuilder corrected = new StringBuilder();

        for (String token : tokens) {
            corrected.append(correct(token)).append(" ");
        }

        return corrected.toString().trim();
    }

    public String correct(String word) {
        if (wordFreq.containsKey(word)) return word;

        Set<String> candidates = edits1(word);
        String best = bestCandidate(candidates);

        if (best != null) return best;

        // fallback: edits2 (optional, expensive)
        for (String e1 : edits1(word)) {
            Set<String> e2 = edits1(e1);
            best = bestCandidate(e2);
            if (best != null) return best;
        }

        return word;
    }

    private Set<String> edits1(String word) {
        Set<String> result = new HashSet<>();
        String letters = "abcdefghijklmnopqrstuvwxyz";

        for (int i = 0; i <= word.length(); i++) {
            String left = word.substring(0, i);
            String right = word.substring(i);

            // deletion
            if (!right.isEmpty()) {
                result.add(left + right.substring(1));
            }

            // transposition
            if (right.length() > 1) {
                result.add(left + right.charAt(1) + right.charAt(0) + right.substring(2));
            }

            // replacement
            if (!right.isEmpty()) {
                for (char c : letters.toCharArray()) {
                    result.add(left + c + right.substring(1));
                }
            }

            // insertion
            for (char c : letters.toCharArray()) {
                result.add(left + c + right);
            }
        }

        return result;
    }

    private String bestCandidate(Set<String> candidates) {
        String best = null;
        int maxFreq = 0;

        for (String w : candidates) {
            Integer freq = wordFreq.get(w);
            if (freq != null && freq > maxFreq) {
                maxFreq = freq;
                best = w;
            }
        }

        return best;
    }

}
