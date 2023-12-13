# Library Management System

## Overview

This repository contains a Library Management System implemented in Scala with a graphical user interface (GUI) using ScalaFX. The system enables users to register, add books, check-in, and return books, with persistent data storage in text files.

## Features

- **User Registration:** Register users with unique IDs and names.
- **Book Addition:** Add books with unique IDs and titles.
- **Book Transactions:** Check out and return books, recording transactions.
- **Reporting:** Generate reports of transactions, user-specific transactions, and display registered users.

## Technologies Used

- **Scala:** Programming language used for implementation.
- **ScalaFX:** Library for building GUI applications in Scala.
- **File I/O:** Persistent storage of user, book, and transaction data.

## Project Structure

### Library Management System (GUI)

- **File:** `LibraryManagementGUI.scala`
- **Description:** Handles user interaction, displays the GUI, and communicates with the `Library` class.

### Library Management System (Core)

- **Files:** `Library.scala`, `User.scala`, `Book.scala`, `Transaction.scala`
- **Description:** Contains the core logic for user and book management, transactions, and data storage.

### User Class

- **File:** `User.scala`
- **Description:** Represents a user in the library with properties such as user ID and name.

### Transaction Classes

- **Files:** `Transaction.scala`
- **Description:** Represents a transaction in the library, recording user actions.

### Book Class

- **File:** `Book.scala`
- **Description:** Represents a book in the library with properties such as book ID, title, and availability status.

## Usage

1. **Run the GUI Application:**

   - Execute the `LibraryManagementGUI` object to launch the GUI.

2. **Interact with the GUI:**

   - Use the GUI to register users, add books, perform transactions, and generate reports.

3. **Data Persistence:**
   - User, book, and transaction data is persistently stored in text files (`User.txt`, `Book.txt`, `Transaction.txt`).

## Setup

1. **Dependencies:**

   - Ensure you have Scala and ScalaFX installed.

2. **Run the Application:**
   - Run the `LibraryManagementGUI` object to start the GUI.

## Contributing

If you would like to contribute or report issues, please create a new branch, make your changes, and submit a pull request.
