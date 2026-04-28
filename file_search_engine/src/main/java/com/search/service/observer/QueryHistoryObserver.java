package com.search.service.observer;

import com.search.model.FileData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;

public class QueryHistoryObserver implements SearchObserver {
    private final Map<String, Integer> queryFrequency = new HashMap<>();

    @Override
    public void onSearch(String query, List<FileData> results) {
        queryFrequency.merge(query, 1, Integer::sum);
    }

    public List<String> suggest(String prefix) {
        return queryFrequency.keySet().stream()
                .filter(q -> q.startsWith(prefix))
                .sorted((a, b) -> queryFrequency.get(b) - queryFrequency.get(a))
                .limit(5)
                .toList();
    }
}
