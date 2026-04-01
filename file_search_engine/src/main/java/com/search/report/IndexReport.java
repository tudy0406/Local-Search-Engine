package com.search.report;

public class IndexReport {

    private final String root;
    private final int insertedFilesCount;
    private final int updatedFilesCount;
    private final int fileCount;
    private final long totalSize;
    private final long durationMs;

    public IndexReport(String root, int insertedFilesCount, int updatedFilesCount, int fileCount, long totalSize, long durationMs) {
        this.root = root;
        this.insertedFilesCount = insertedFilesCount;
        this.updatedFilesCount = updatedFilesCount;
        this.fileCount = fileCount;
        this.totalSize = totalSize;
        this.durationMs = durationMs;
    }


    public void print() {

        System.out.println("\n===== Indexing Report =====");

        System.out.println("Root: " + this.root);
        System.out.println("Files inserted: " + this.insertedFilesCount);
        System.out.println("Files updated: " + this.updatedFilesCount);
        System.out.println("Total files indexed: " + this.fileCount);

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
    public int getFileCount() { return fileCount; }
    public long getTotalSize() { return totalSize; }
    public long getDurationMs() { return durationMs; }
}