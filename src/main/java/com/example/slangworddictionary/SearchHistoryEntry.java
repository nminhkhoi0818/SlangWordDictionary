package com.example.slangworddictionary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SearchHistoryEntry {
    private String searchTerm;
    private String timestamp;

    public SearchHistoryEntry (String searchTerm, LocalDateTime timestamp) {
        this.searchTerm = searchTerm;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        this.timestamp = timestamp.format(formatter);
    }

    public SearchHistoryEntry(String searchTerm) {
        this.searchTerm = searchTerm;
        LocalDateTime timestampNow = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        this.timestamp = timestampNow.format(formatter);
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
