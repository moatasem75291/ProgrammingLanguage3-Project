# Library Management System 📚💻

The Library Management System is a Scala-based application designed to manage the operations of a library. It provides functionalities for registering users, adding books, handling check-out and return of books, generating reports, and more. The system is built using Scala and ScalaFX for the graphical user interface, and it incorporates the Akka actor system for handling system messages.

## Table of Contents
- [Features](#features)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Database](#database)
- [Dependencies](#dependencies)
- [Contributing](#contributing)
- [Acknowledgments](#acknowledgments)
- [Project Structure](#project-structure)
- [Contribute](#contribute)
- [Show your support](#show-your-support)
- [License](#license)

## Features
- **User Registration:** Register new users with unique IDs and names.
- **Book Management:** Add new books with unique IDs and titles.
- **Check-Out and Return:** Manage the check-out and return of books, updating availability status.
- **Reports:** Generate transaction reports, user-specific reports, and display lists of registered users and books.
- **Graphical User Interface:** Utilizes ScalaFX for a user-friendly interface.
- **Akka Actor System:** Incorporates an Akka actor system for handling system messages and graceful shutdown.

## Getting Started
 **Clone the Repository:**
   ```bash
   git clone https://github.com/your-username/LibraryManagementSystem.git
   cd LibraryManagementSystem
   ```

## Usage
Upon running the application, a graphical user interface will be displayed. Use the provided options to register users, add books, perform check-out and return operations, generate reports, and more. Follow the on-screen prompts for user input.

## Database
The application uses an SQLite database to persist data. Database initialization and table creation are handled during the system's startup. The database file is located at `database.db` by default.

## Dependencies
   - Scala
   - ScalaFX
   - Akka Actor System
   - SQLite JDBC Driver

## Contributing
   - Fork the repository.
   - Create a new branch for your feature or bug fix: git checkout -b feature/my-feature.
   - Make your changes and commit them: git commit -m 'Add new feature'.
   - Push to the branch: git push origin feature/my-feature.
   - Submit a pull request.

## Acknowledgments
Special thanks to the Scala, ScalaFX, and Akka communities for their valuable contributions. The SQLite JDBC Driver has been instrumental in enabling database connectivity.

## Project Structure
``` CSS
LibraryManagementSystem/
├── src/
│   ├── main/
│   │   └── scala/
│   │       └── com/
│   │           └── yourusername/
│   │               └── librarymanagementsystem/
│   │                   ├── LibraryManagementGUI.scala
│   │                   ├── Library.scala
│   │                   ├── Book.scala
│   │                   ├── User.scala
│   │                   ├── Transaction.scala
│   │                   ├── LibraryActors.scala
│   │                   └── ... (other source files)
│   └── resources/
│       └── application.conf
├── target/
├── project/
│   └── build.properties
├── build.sbt
└── README.md
```

## Contribute
Contributions are welcome! Please review the contribution guidelines for details.

## Show your support
Give a ⭐️ if you found this project helpful or consider sponsoring the project.

## License
This project is licensed under the MIT License.

```python
Feel free to make any further adjustments as needed for your project.
```
