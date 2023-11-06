package com.example.slangworddictionary;

import java.time.LocalDateTime;

public class SearchHistoryEntry {
    private String searchTerm;
    private LocalDateTime timestamp;

    public SearchHistoryEntry (String searchTerm, LocalDateTime timestamp) {
        this.searchTerm = searchTerm;
        this.timestamp = timestamp;
    }

    public SearchHistoryEntry(String searchTerm) {
        this.searchTerm = searchTerm;
        this.timestamp = LocalDateTime.now();
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
