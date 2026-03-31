package com.search.sync;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface FileCrawler {
    List<Path> crawl(Path root, Set<String> excludedExtensions);
}
