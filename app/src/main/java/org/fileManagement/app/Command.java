package org.fileManagement.app;

import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Command {

  public static void handleCommand(User currentUser) {
    try (Scanner scanner = new Scanner(System.in)) {
      System.out.println("FILE MANAGEMENT SYSTEM");
      System.out.println("commands: exit, quit, login, register, borrow, return, list books, add book, report, logout");
      while (true) {

        System.out.print(">>> ");

        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit"))
          break;

        if (input.equalsIgnoreCase("man") || input.equalsIgnoreCase("manual") || input.equalsIgnoreCase("help")) {
          String cwd = System.getProperty("user.dir");
          StringBuilder manualPath = new StringBuilder();
          manualPath.append(cwd);
          manualPath.append("/manual.txt");
          Scanner reader = new Scanner(new FileReader(manualPath.toString()));
          while (reader.hasNextLine()) {
            System.out.println(reader.nextLine());
          }
          reader.close();
        }

        String[] commands = input.split("\\s+");

        if (commands.length == 0)
          continue;

        String command = commands[0];

        Library lib = new Library();

        switch (command) {
          case "register":
            if (commands.length < 3 || 3 > commands.length) {
              System.out.println("Usage: register <username> <password>");
              continue;
            }
            Member newMember = new Member();
            Boolean memberExists = newMember.new_(commands[1].toLowerCase(), commands[2]);
            if (memberExists) {
              System.out.println("username already exists");
            } else {
              newMember.save();
            }

            System.out.println(newMember.toString());
            continue;
          case "login":
            if (commands.length < 3 || 3 > commands.length) {
              System.out.println("Usage: login <username> <password>");
              continue;
            }
            Pair<Boolean, Member> pair = Member.login(commands[1], commands[2]);
            if (!pair.first) {
              System.out.println("Invalid credentials");
              return;
            }
            currentUser = pair.second;
            System.out.println(currentUser);
            continue;
          case "list":
            if (commands.length < 2 || 2 > commands.length) {
              System.out.println("invalid command");
              System.out.println("Usage: list books");
              System.out.println("Usage: list users");
              continue;
            }
            if (currentUser == null) {
              System.out.println("Unauthorized");
              continue;
            }
            if (commands[1].trim().equalsIgnoreCase("books")) {

              List<Book> books = lib.search("", false, false);
              System.out.println(Arrays.toString(books.toArray()));
              continue;
            }
            if (commands[1].trim().equalsIgnoreCase("users")) {
              if ((currentUser != null) && currentUser.getClass() == Member.class) {
                System.out.println("Unauthorized. Admins Only");
                continue;
              }
              continue;
            }

            System.out.println("Usage: list books");
            System.out.println("Usage: list users");
            System.out.println(commands[1].trim().equalsIgnoreCase("users"));
            continue;
          case "borrow":
            if (commands.length < 2) {
              System.out.println("Usage borrow <book title>");
              continue;
            }
            if (currentUser == null) {
              System.out.println("Unauthorized");
              continue;
            }
            StringBuilder title = new StringBuilder();
            int idx = 0;
            for (String s : commands) {
              if (idx > 0) {
                title.append(s);
                title.append(" ");
              }
              idx++;
            }
            Optional<Book> foundBook = lib.borrow(title.toString().trim());
            if (foundBook.isPresent()) {
              System.out.println(foundBook.toString());
              continue;
            }
            System.out.println(title + " is borrowed already or does not exist.");
            continue;
          default:
            System.out.println("invalid command");

        }
      }
    } catch (Exception exc) {
      System.out.println("Error: " + exc.getMessage());
    }
  }

}
