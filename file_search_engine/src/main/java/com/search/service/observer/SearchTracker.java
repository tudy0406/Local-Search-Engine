package com.search.service.observer;

import com.search.model.FileData;

import java.util.ArrayList;
import java.util.List;

public class SearchTracker {
    private final List<SearchObserver> observers = new ArrayList<>();

    public void addObserver(SearchObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(String query, List<FileData> results) {
        for (SearchObserver o : observers) {
            o.onSearch(query, results);
        }
    }
}
