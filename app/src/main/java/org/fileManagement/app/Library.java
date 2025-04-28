package org.fileManagement.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.fileManagement.utilities.JsonUtils;

class Library {
  private Map<String, Book> books;

  private void setBooks() {
    this.books = new HashMap<>();
  }

  private Map<String, Book> getBooks() {
    return this.books;
  }

  public void loadBooksFromFile(Boolean isBorrowed, Boolean freeBooks) throws Exception {
    List<Book> LoadedBooks = JsonUtils.readObjectsFromJsonFile("library.json", Book.class);
    if (isBorrowed && freeBooks) {
      throw new Exception("cannot fetch borrowed and free books simultaneourly");
    }
    if (getBooks() == null) {
      setBooks();
    }
    if (!isBorrowed && !freeBooks) {
      for (Book book : LoadedBooks) {
        this.books.put(book.getTitle(), book);
      }
    }
    if (isBorrowed) {
      for (Book book : LoadedBooks) {
        if (book.getIsBorrowed()) {
          this.books.put(book.getTitle(), book);
        }
      }
    }
    if (freeBooks) {
      for (Book book : LoadedBooks) {
        if (!book.getIsBorrowed()) {
          this.books.put(book.getTitle(), book);
        }
      }
    }
  }

  public void saveBooksToFile(String path) {
    /* ... */ }

  public List<Book> search(String query, Boolean isBorrowed, Boolean isFree) {
    List<Book> foundBooks = new ArrayList<>();

    try {
      loadBooksFromFile(isBorrowed, isFree);

      if (query == "") {
        for (Map.Entry<String, Book> entry : this.books.entrySet()) {
          foundBooks.add(entry.getValue());
        }

      } else {
        // System.out.println(this.books.size());
        for (Map.Entry<String, Book> book : this.books.entrySet()) {
          if (query.equalsIgnoreCase(book.getKey())) {
            foundBooks.add(book.getValue());
          }
        }
      }
      return foundBooks;
    } catch (Exception e) {
      e.printStackTrace();
      return foundBooks;
    }
  }

  public Optional<Book> borrow(String title) {
    // finds the searched book and loads free-books to this.books
    List<Book> bookExists = this.search(title, false, true);

    List<Book> booksToWrite = new ArrayList<>();

    if (bookExists.size() > 0) {

      Book book = bookExists.get(0);
      book.setIsBorrowed(true);
      try {
        // loads all books to this.books
        loadBooksFromFile(false, false);
      } catch (Exception e) {
        e.printStackTrace();
      }
      // remove the searched book
      this.books.remove(book.getTitle());
      // add the searched book with updated isBorrowed
      this.books.put(book.getTitle(), book);
      // set back as a list for serialization
      for (Map.Entry<String, Book> entry : this.books.entrySet()) {
        booksToWrite.add(entry.getValue());
      }

      // write to file with updated searched book
      JsonUtils.writeObjectToJsonFile(booksToWrite, "library.json");
      return Optional.ofNullable(book);
    }

    return Optional.empty();
  }
}

class Book {
  private String title;
  private String author;
  private String isbn;
  private boolean isBorrowed;

  public Book() {
  }

  @Override
  public String toString() {
    StringBuilder newStr = new StringBuilder();
    newStr.append("title: ");
    newStr.append(title);

    newStr.append(", author: ");
    newStr.append(author);

    newStr.append(", isbn: ");
    newStr.append(isbn);

    newStr.append(", isBorrowed: ");
    newStr.append(isBorrowed);
    return newStr.toString();
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String newTitle) {
    this.title = newTitle;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String newAuthor) {
    this.author = newAuthor;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String newIsbn) {
    this.isbn = newIsbn;
  }

  public Boolean getIsBorrowed() {
    return isBorrowed;
  }

  public void setIsBorrowed(Boolean newIsBorrowed) {
    this.isBorrowed = newIsBorrowed;
  }
}