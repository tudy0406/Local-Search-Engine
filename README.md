# Local-Search-Engine

# Local File Search Engine

## Overview

This project implements a local file search engine that indexes files from a specified directory and enables fast searching using full-text search capabilities provided by SQLite (FTS5).

The system is designed with a modular architecture, separating concerns between indexing (sync layer), searching (service layer), and user interaction (CLI).

---

## Features

- Recursive file system crawling
- Filtering by file extensions and ignored directories (configurable at runtime)
- Content extraction for textual files
- Metadata extraction (size, extension, last modified time)
- Full-text search using SQLite FTS5
- Search over file content and filenames
- Query normalization (handles punctuation and multiple terms)
- Incremental indexing (only new or modified files are processed)
- Indexing report generation (files count, total size, duration)
- Command-line interface for interaction

---

## Use Cases

### 1. Index Local Files

The application scans a user-defined root directory and indexes files into a database.

- Ignores unwanted file types (e.g., `.jpg`, `.exe`)
- Skips specified directories (e.g., `.git`, `node_modules`)
- Extracts file content and metadata
- Stores data in SQLite with full-text indexing

---

### 2. Search by Content

Users can search for keywords that appear inside files.

Example:
```
database indexing
```

This returns files whose content contains both terms.

---

### 3. Search by Filename

Since filenames are indexed in the FTS table, users can also search by file name.

Example:
```
report
```

Matches:
- `report.txt`
- files containing "report" in content

---

### 4. Incremental Indexing

The system avoids reprocessing unchanged files by:

- Checking last modified timestamps
- Inserting only new files
- Updating only modified files
- Skipping unchanged files

---

### 5. Indexing Report

After indexing, the system generates a report containing:

- Root directory
- Number of inserted files
- Number of updated files
- Total size of indexed files
- Time taken

---

### 6. Interactive CLI Search

Users interact with the system via a command-line interface:

```
Search > drum
Search > xml
Search > exit
```

---

## Architecture

The system is structured into multiple layers:

### Sync Layer
- `FileCrawler` – traverses the file system
- `FileReader` – extracts file content
- `MetadataExtractor` – extracts metadata
- `SyncService` – orchestrates indexing
- `DatabaseAdapter` – handles insert/update operations

### Service Layer
- `QueryParser` – processes user queries
- `SearchService` – coordinates search logic
- `SearchDatabaseAdapter` – executes FTS queries
- `ResultFormatter` – formats output

### UI Layer
- `InputHandler` – reads user input
- `View` – displays messages and results

### Database Layer
- SQLite database with:
  - `files` table (metadata + content)
  - `files_fts` virtual table (full-text index)
  - triggers for automatic synchronization

---

## Configuration

The application supports runtime configuration via a properties file.

Example:

```
root.dir=C:/path/to/files
ignore.extensions=.jpg,.png,.exe
ignore.directories=.git,node_modules
```

---

## Technologies Used

- Java
- SQLite
- SQLite FTS5 (Full-Text Search)
- JDBC

---

## Limitations

- Metadata-based search (e.g., filtering by size or extension) is not implemented
- Only textual files are indexed (binary files are skipped)
- No ranking or relevance scoring beyond basic FTS behavior
- No GUI (CLI only)

---

## How to Run

1. Configure the application using the properties file
2. Run the application
3. Wait for indexing to complete
4. Use the CLI to search for files
5. Type `exit` to quit

---

## Future Improvements

- Metadata-based search (size, extension filters)
- Result ranking and relevance scoring
- Highlighting matched terms
- GUI interface
- File preview enhancements