package com.search.sync;

import com.search.model.FileData;

import java.nio.file.Path;
import java.util.Set;

public class SyncService {
    private final FileCrawler crawler;
    private final FileReader reader;
    private final MetadataExtractor metadata;
    private final DatabaseAdapter db;

    public SyncService(FileCrawler crawler, FileReader reader, MetadataExtractor metadata, DatabaseAdapter db) {
        this.crawler = crawler;
        this.reader = reader;
        this.metadata = metadata;
        this.db = db;
    }

    public void sync(Path root, Set<String> excludedExtensions) {

        var files = crawler.crawl(root, excludedExtensions);

        for (Path path : files) {
            try {
                String content = reader.readContent(path);
                if (content == null) continue;

                FileData data = new FileData(
                        path,
                        content,
                        metadata.getSize(path),
                        metadata.getModifiedTime(path),
                        metadata.getExtension(path)
                );

                db.insertFile(data);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
