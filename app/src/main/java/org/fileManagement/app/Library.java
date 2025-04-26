package org.fileManagement.app;

import java.util.Map;

class Library {
  private Map<String, Book> books;

  public void loadBooksFromFile(String path) {
    /* ... */ }

  public void saveBooksToFile(String path) {
    /* ... */ }

  // public List<Book> search(String query) {
  // }
}

class Book {
  private String title;
  private String author;
  private String isbn;
  private boolean isBorrowed;
  // Getters and setters
}