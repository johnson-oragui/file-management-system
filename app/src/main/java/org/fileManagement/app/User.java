package org.fileManagement.app;

import java.util.*;

import org.fileManagement.utilities.AuthUtils;
import org.fileManagement.utilities.JsonUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

class Pair<T, V> {
  public T loginSuccess;
  public V currentUser;

  public Pair(T loginSuccess, V currentUser) {
    this.loginSuccess = loginSuccess;
    this.currentUser = currentUser;
  }
}

public abstract class User {
  protected String username;
  protected String password;
  protected Integer id;

  public abstract void save();

  public abstract String toString();
}

class Admin extends User {

  public void showMenu() {
  }

  public void addBook(Book book) {
    /* ... */ }

  public void removeBook(String isbn) {
    /* ... */ }

  public String toString() {
    return getUsername();
  }

  public void save() {
    JsonUtils.addObjectToJsonFile("admin.json", this, Admin.class);
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}

class Member extends User {

  @JsonProperty("borrowedBooks")
  @JsonInclude(Include.NON_EMPTY)
  private List<Book> borrowedBooks = new ArrayList<>();

  public Member() {
  }

  public void returnBook(Book book) {
    /* ... */ }

  @Override
  public String toString() {
    return username;
  }

  // Persists Member
  public void save() {
    JsonUtils.addObjectToJsonFile("member.json", this, Member.class);
  }

  // Creates a new Member
  public Boolean new_(String username, String password) {
    List<Member> objects = JsonUtils.readObjectsFromJsonFile("member.json", Member.class);
    for (Member object : objects) {
      if (object.getUsername().equalsIgnoreCase(username)) {
        return true;
      }
    }
    setId(objects.size());
    setPassword(password);
    setUsername(username);
    return false;
  }

  // login Member
  public static Pair<Boolean, Member> login(String username, String password) {
    List<Member> members = JsonUtils.readObjectsFromJsonFile("member.json", Member.class);
    for (Member member : members) {

      if (member.getUsername().equalsIgnoreCase(username)
          && AuthUtils.verifyPassword(password, AuthUtils.hashPassword(password))) {

        Member existMember = new Member();
        existMember.setId(member.getId());
        existMember.setPassword(member.getPassword());
        existMember.setUsername(member.getUsername());
        existMember.setBorrowedBooks(member.getBorrowedBooks());
        return new Pair<>(true, existMember);
      }
    }
    return new Pair<>(false, null);
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = AuthUtils.hashPassword(password);
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer newId) {
    this.id = newId;
  }

  public void setBorrowedBooks(List<Book> books) {
    this.borrowedBooks = books != null ? books : new ArrayList<>();
  }

  public List<Book> getBorrowedBooks() {
    return this.borrowedBooks;
  }

  public void removeBorrowedBooks(String title) {
    List<Book> borrowed = new ArrayList<>();
    for (Book book : this.getBorrowedBooks()) {
      if (!book.getTitle().equalsIgnoreCase(title.trim())) {
        borrowed.add(book);
      }

    }
    this.setBorrowedBooks(borrowed);
  }

  // Method to add single book
  public Boolean addBorrowedBook(Book book) {

    for (Book book_ : this.borrowedBooks) {
      if (book_.getTitle().equalsIgnoreCase(book.getTitle())) {
        return false; // book already borrowed
      }
    }

    this.borrowedBooks.add(book);

    List<Member> members = JsonUtils.readObjectsFromJsonFile("member.json", Member.class);
    for (Member member : members) {
      if (member.getUsername().equalsIgnoreCase(this.getUsername())) {
        member.borrowedBooks.clear();
        member.setBorrowedBooks(borrowedBooks);
      }
    }
    JsonUtils.writeObjectToJsonFile(members, "member.json");
    return true;

  }

  public Boolean returnBook(String title) {
    int borrowedBookSize = this.getBorrowedBooks().size();
    if (borrowedBookSize == 0) {
      return false;
    }
    removeBorrowedBooks(title);

    List<Member> members = JsonUtils.readObjectsFromJsonFile("member.json", Member.class);
    Iterator<Member> membersIter = members.iterator();

    while (membersIter.hasNext()) {
      Member mem = membersIter.next();
      if (mem.getUsername().equalsIgnoreCase(this.getUsername())) {
        mem.setBorrowedBooks(this.getBorrowedBooks());
      }
    }
    if (borrowedBookSize == this.getBorrowedBooks().size()) {
      return false;
    }

    JsonUtils.writeObjectToJsonFile(members, "member.json");

    return true;
  }

}