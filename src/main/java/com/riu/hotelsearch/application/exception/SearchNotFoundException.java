package com.riu.hotelsearch.application.exception;

public class SearchNotFoundException extends RuntimeException {
    public SearchNotFoundException(String searchId) {
        super("Search not found: " + searchId);
    }
}