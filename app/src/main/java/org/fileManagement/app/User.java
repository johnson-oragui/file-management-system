package org.fileManagement.app;

import java.util.*;

import org.fileManagement.utilities.AuthUtils;
import org.fileManagement.utilities.JsonUtils;

class Pair<T, V> {
  public T first;
  public V second;

  public Pair(T first, V second) {
    this.first = first;
    this.second = second;
  }
}

public abstract class User {
  protected String username;
  protected String password;
  protected Integer id;

  public abstract void showMenu();

  public abstract void save();

  public abstract String toString();
}

class Admin extends User {

  @Override
  public void showMenu() {
  }

  public void addBook(Book book) {
    /* ... */ }

  public void removeBook(String isbn) {
    /* ... */ }

  public String toString() {
    StringBuilder str = new StringBuilder();
    str.append(username);
    str.append(password);
    return str.toString();
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
  private List<Book> borrowedBooks = new ArrayList<>();

  @Override
  public void showMenu() {
  }

  public void borrowBook(Book book) {
    /* ... */ }

  public void returnBook(Book book) {
    /* ... */ }

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
      if (object.getUsername().equals(username)) {
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
        existMember.setId(member.id);
        existMember.setPassword(member.password);
        existMember.setUsername(member.username);
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

}