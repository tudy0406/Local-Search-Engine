package com.search.sync;

import com.search.config.ConfigLoader;
import com.search.model.FileData;
import com.search.report.IndexReport;
import com.search.sync.words_processing.TextProcessor;
import com.search.sync.words_processing.WordFrequencyService;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;

public class SyncService {
    private final ConfigLoader configuration;
    private final FileCrawler crawler;
    private final FileReader reader;
    private final MetadataExtractor metadata;
    private final DatabaseAdapter db;
    private final WordFrequencyService wordFrequencyService;

    public SyncService(ConfigLoader configuration ,FileCrawler crawler,
                       FileReader reader,
                       MetadataExtractor metadata,
                       DatabaseAdapter db,
                       WordFrequencyService wordFrequencyService) {
        this.configuration = configuration;
        this.crawler = crawler;
        this.reader = reader;
        this.metadata = metadata;
        this.db = db;
        this.wordFrequencyService = wordFrequencyService;
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
                if (storedModified.isEmpty() || storedAccessed.isEmpty()) {
                    FileData data = buildFileData(path);
                    db.insert(data);

                    wordFrequencyService.insertWords(TextProcessor.countWords(data.getContent()));

                    insertedFilesCount++;
                    totalSize += size;
                }
                else if (storedModified.get() != currentModified) {
                    FileData data = buildFileData(path);
                    db.update(data);

                    Map<String, Integer> newWords = TextProcessor.countWords(data.getContent());
                    Map<String, Integer> oldWords = db.getFileWordCount(path);

                    Set<String> allWords = new HashSet<>();
                    allWords.addAll(newWords.keySet());
                    allWords.addAll(oldWords.keySet());

                    for (String word : allWords) {
                        int newCount = newWords.getOrDefault(word, 0);
                        int oldCount = oldWords.getOrDefault(word, 0);

                        int delta = newCount - oldCount;

                        if (delta != 0) {
                            db.insertOrUpdateWord(word, delta);
                        }
                    }

                    db.deleteFileWords(path);

                    for (var entry : newWords.entrySet()) {
                        db.insertFileWord(path, entry.getKey(), entry.getValue());
                    }

                    updatedFilesCount++;
                    totalSize += size;
                }else if(storedAccessed.get() != currentAccessed){
                    db.updateLastAccessed(path, currentAccessed);
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

    public Map<String, Integer> loadWordsFrequencies() {
        try{
            return db.loadWordFrequencies();}
        catch(SQLException e){
            System.out.println("[SQL ERROR] Retrieving words frequncies failed: " + e.getMessage());
        };
        return null;
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