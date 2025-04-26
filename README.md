# File Management System (Java)

This is a **Java console application** that allows users to:

- Register and login (passwords are securely **hashed** with **Bcrypt** 🔒)
- Borrow and return books
- List available books
- Add new books
- Report on system status
- Logout

It’s a lightweight "mini-shell" built using Java **OOP principles**.

---

## 📁 Project Structure

```
/src/main/
  Main.java
  Util.java
  User.java
  Library.java

/java-libs/
  jbcrypt-0.4.jar

/build/
  *.class
```

---

## 🚀 How to Run

> You must have **Java 17+** installed.

1. **Clone the project:**

```bash
git clone https://github.com/johnson-oragui/file-management-system.git
cd file-management-system
```

2. **Compile the project:**

```bash
./gradlew clean build
```

2. **Run the jar**

```bash
java -jar app/build/libs/app.jar
```

✅ **No external dependencies are needed**.  
The required Bcrypt library (`jbcrypt-0.4.jar`) is already included in the `/java-libs/` folder.

---

## 🔥 Features

- **Secure password storage** using **Bcrypt hashing**.
- **Command-driven interface** using `Scanner` input.
- **Proper error handling** and **custom exception classes**.
- **Java Collections**: `HashMap`, `ArrayList`, etc.
- **OOP structure** for maintainability and scalability.

---

## 🛠 Technologies Used

- Java (17 or higher)
- Bcrypt (`jbcrypt-0.4.jar` from Maven Central)

---

## 📋 Example Commands (inside the shell)

| Command      | Description                 |
| :----------- | :-------------------------- |
| `register`   | Register a new user         |
| `login`      | Log in as a user            |
| `borrow`     | Borrow a book               |
| `return`     | Return a book               |
| `list books` | List all available books    |
| `add book`   | Add a new book (admin only) |
| `report`     | Show system report          |
| `logout`     | Logout of the system        |

Use `help` or `man` inside the app to see command usages!

---

# 📢 Notes

- **Passwords** are hashed **before saving**, and **never stored as plain text**.
- You **don't need to install anything separately** — everything needed is inside the project.

---

# 📜 License

This project is open source under the MIT License.
