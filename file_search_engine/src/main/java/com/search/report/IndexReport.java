package com.search.report;

public class IndexReport {

    private final String root;
    private final int insertedFilesCount;
    private final int updatedFilesCount;
    private final int indexedFileCount;
    private final int skippedFileCount;
    private final long totalSize;
    private final long durationMs;

    public IndexReport(String root, int insertedFilesCount, int updatedFilesCount, int indexedFileCount, int skippedFileCount,long totalSize, long durationMs) {
        this.root = root;
        this.insertedFilesCount = insertedFilesCount;
        this.updatedFilesCount = updatedFilesCount;
        this.indexedFileCount = indexedFileCount;
        this.skippedFileCount = skippedFileCount;
        this.totalSize = totalSize;
        this.durationMs = durationMs;
    }


    public void print() {

        System.out.println("\n===== Indexing Report =====");

        System.out.println("Root: " + this.root);
        System.out.println("Files inserted: " + this.insertedFilesCount);
        System.out.println("Files updated: " + this.updatedFilesCount);
        System.out.println("Total files indexed: " + this.indexedFileCount);
        System.out.println("Total files skipped: " + this.skippedFileCount);
        System.out.println("Total size: " + formatSize(this.totalSize));
        System.out.println("Time taken: " + this.durationMs + " ms");

        System.out.println("===========================\n");
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return (bytes / 1024) + " KB";
        return (bytes / (1024 * 1024)) + " MB";
    }

    public String getRoot() { return root; }
    public int getInsertedFilesCount() { return insertedFilesCount; }
    public int getUpdatedFilesCount() { return updatedFilesCount; }
    public int getIndexedFileCount() { return indexedFileCount; }
    public int getSkippedFileCount() { return skippedFileCount; }
    public long getTotalSize() { return totalSize; }
    public long getDurationMs() { return durationMs; }
}