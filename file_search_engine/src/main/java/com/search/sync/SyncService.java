package com.search.sync;

import com.search.config.ConfigLoader;
import com.search.model.FileData;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

public class SyncService {
    private final ConfigLoader configuration;
    private final FileCrawler crawler;
    private final FileReader reader;
    private final MetadataExtractor metadata;
    private final DatabaseAdapter db;

    public SyncService(ConfigLoader configuration ,FileCrawler crawler,
                       FileReader reader,
                       MetadataExtractor metadata,
                       DatabaseAdapter db) {
        this.configuration = configuration;
        this.crawler = crawler;
        this.reader = reader;
        this.metadata = metadata;
        this.db = db;
    }

    public void sync() {

        var files = crawler.crawl(Path.of(configuration.getRootDir()), configuration.getIgnoredExtensions(), configuration.getIgnoredDirectories());

        for (Path path : files) {
            try {
                long currentModified = metadata.getModifiedTime(path);
                Optional<Long> storedModified = db.getLastModified(path);

                if (storedModified.isEmpty()) {
                    FileData data = buildFileData(path);
                    db.insert(data);
                }

                else if (storedModified.get() != currentModified) {
                    FileData data = buildFileData(path);
                    db.update(data);
                }

                else { //skip file
                }

            } catch (Exception e) {
                //System.out.println("Error processing file: " + path);
                System.out.println(e.getMessage());
            }
        }
    }


    private FileData buildFileData(Path path) throws Exception {

        String content = reader.readContent(path);
        if (content == null) {
            throw new RuntimeException("Failed to read content for: " + path);
        }

        return new FileData(
                path,
                content,
                metadata.getSize(path),
                metadata.getModifiedTime(path),
                metadata.getExtension(path)
        );
    }
}