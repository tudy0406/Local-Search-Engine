package com.search.sync;

import com.search.config.ConfigLoader;
import com.search.model.FileData;
import com.search.report.IndexReport;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
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

    public IndexReport sync() {
        String root = configuration.getRootDir();
        int insertedFilesCount=0;
        int updatedFilesCount=0;
        int skippedFilesCount=0;
        int indexedfileCount;
        long totalSize=0;
        long size;
        long startTime = System.currentTimeMillis();
        var files = crawler.crawl(Path.of(configuration.getRootDir()), configuration.getIgnoredExtensions(), configuration.getIgnoredDirectories());

        for (Path path : files) {
            try {

                long currentModified = metadata.getModifiedTime(path);
                long currentAccessed = metadata.getLastAccessed(path);
                Optional<Long> storedModified = db.getLastModified(path);
                Optional<Long> storedAccessed = db.getLastAccessed(path);

                size = metadata.getSize(path);
                if (storedModified.isEmpty() ||  storedAccessed.isEmpty()) {
                    FileData data = buildFileData(path);
                    db.insert(data);
                    insertedFilesCount++;
                    totalSize += size;
                }
                else if (storedModified.get() != currentModified ||  storedAccessed.get() != currentAccessed) {
                    FileData data = buildFileData(path);
                    db.update(data);
                    updatedFilesCount++;
                    totalSize += size;
                }
                else {
                    skippedFilesCount++;
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        long duration = System.currentTimeMillis() - startTime;

        indexedfileCount = insertedFilesCount + updatedFilesCount;
        return new IndexReport(
                root,
                insertedFilesCount,
                updatedFilesCount,
                indexedfileCount,
                skippedFilesCount,
                totalSize,
                duration
        );
    }


    private FileData buildFileData(Path path) throws Exception {

        String content = reader.readContent(path);
        if (content == null) {
            throw new RuntimeException("Failed to read content for: " + path);
        }

        long lastSearched;
        if(db.getLastSearched(path).isPresent()) {
            lastSearched = db.getLastSearched(path).get();
        }else{
            lastSearched = 0L;
        }

        return new FileData(
                path,
                content,
                metadata.getSize(path),
                metadata.getModifiedTime(path),
                metadata.getLastAccessed(path),
                lastSearched,
                metadata.getExtension(path)
        );
    }
}