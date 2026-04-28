package com.search.service.observer;

import com.search.model.FileData;
import java.util.List;

public interface SearchObserver {

    void onSearch(String query, List<FileData> results);
    List<String> suggest(String prefix);
}
