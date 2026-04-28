package com.search.sync.words_processing;

import com.search.sync.DatabaseAdapter;

import java.sql.SQLException;
import java.util.*;

public class WordFrequencyService {

    private final Map<String, Integer> wordFreq = new HashMap<>();
    private final DatabaseAdapter db;

    public WordFrequencyService(DatabaseAdapter db) {
        this.db = db;
    }

    public Map<String, Integer> getWordFreq() {
        return wordFreq;
    }

    public void load(Map<String, Integer> data) {
        wordFreq.clear();
        wordFreq.putAll(data);
    }

    public void insertWords(Map<String, Integer> words) throws SQLException {
        for (var entry : words.entrySet()) {
            db.insertOrUpdateWord(entry.getKey(), entry.getValue());
        }
    }
}