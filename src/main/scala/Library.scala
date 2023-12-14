import scala.collection.mutable.ArrayBuffer
import java.text.SimpleDateFormat
import java.util.Date
import java.sql.{Connection, DriverManager,PreparedStatement, ResultSet}

class Library {
  // JDBC URL, username, and password of SQLite server
  private val url = "jdbc:sqlite:C:\\Users\\MBR\\IdeaProjects\\LibraryManagementSystem\\database.db"
  private val connection: Connection = DriverManager.getConnection(url)
  private var users = Map[Int, User]()
  private var books = Map[Int, Book]()
  private val transactions = ArrayBuffer[Transaction]()
  def getUsers: Map[Int, User] = users
  def getBooks: Map[Int, Book] = books
  def userExists(userId: Int): Boolean = users.contains(userId)
  def bookExists(bookId: Int): Boolean = books.contains(bookId)

  // Initialize tables if not exist
  initDatabase()

  private def initDatabase(): Unit = {
    val statement = connection.createStatement()

    // Users table
    statement.execute(
      """
        |CREATE TABLE IF NOT EXISTS users (
        |  user_id INTEGER PRIMARY KEY,
        |  name TEXT NOT NULL
        |);
      """.stripMargin
    )

    // Books table
    statement.execute(
      """
        |CREATE TABLE IF NOT EXISTS books (
        |  book_id INTEGER PRIMARY KEY,
        |  title TEXT NOT NULL,
        |  is_available INTEGER NOT NULL
        |);
      """.stripMargin
    )

    // Transactions table
    statement.execute(
      """
        |CREATE TABLE IF NOT EXISTS transactions (
        |  transaction_id INTEGER PRIMARY KEY,
        |  user_id INTEGER,
        |  book_id INTEGER,
        |  action TEXT NOT NULL,
        |  timestamp TEXT NOT NULL,
        |  FOREIGN KEY (user_id) REFERENCES users(user_id),
        |  FOREIGN KEY (book_id) REFERENCES books(book_id)
        |);
      """.stripMargin
    )

    statement.close()
  }

  def loadFromFile(): Unit = {
    loadUsersFromDatabase()
    loadBooksFromDatabase()
    loadTransactionsFromDatabase()
  }

  private def loadUsersFromDatabase(): Unit = {
    val connection = DriverManager.getConnection(url)
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT * FROM users")

    while (resultSet.next()) {
      val userId = resultSet.getInt("user_id")
      val userName = resultSet.getString("name")
      users += (userId -> new User(userId, userName))
    }

    resultSet.close()
    statement.close()
    connection.close()
  }

  private def loadBooksFromDatabase(): Unit = {
    val connection = DriverManager.getConnection(url)
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT * FROM books")

    while (resultSet.next()) {
      val bookId = resultSet.getInt("book_id")
      val title = resultSet.getString("title")
      val isAvailable = resultSet.getBoolean("is_available")
      books += (bookId -> new Book(bookId, title, isAvailable))
    }

    resultSet.close()
    statement.close()
    connection.close()
  }

  private def loadTransactionsFromDatabase(): Unit = {
    val connection = DriverManager.getConnection(url)
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT * FROM transactions")

    while (resultSet.next()) {
      val userId = resultSet.getInt("user_id")
      val bookId = resultSet.getInt("book_id")
      val action = resultSet.getString("action")
      val timestamp = resultSet.getString("timestamp")

      if (users.contains(userId) && books.contains(bookId)) {
        transactions += new Transaction(users(userId), books(bookId), action, timestamp)
      } else {
        println(s"Skipping transaction: User with ID $userId or Book with ID $bookId not found.")
      }
    }

    resultSet.close()
    statement.close()
    connection.close()
  }

  def registerUser(userId: Int, name: String): String = {
    if (!userExists(userId)) {
      try {
        // Insert the new user into the 'users' table
        val insertUserSQL = "INSERT INTO users (user_id, name) VALUES (?, ?)"
        val insertUserStatement: PreparedStatement = connection.prepareStatement(insertUserSQL)
        insertUserStatement.setInt(1, userId)
        insertUserStatement.setString(2, name)
        insertUserStatement.executeUpdate()
        users += (userId -> new User(userId, name))
        s"User '$name' registered with ID '$userId'."
      } catch {
        case e: Exception =>
          s"Error registering user: ${e.getMessage}"
      }
    } else {
      s"User with ID '$userId' already exists."
    }
  }
  def addBook(bookId: Int, title: String): String = {
    if (!bookExists(bookId)) {
      try {
        val insertBookSQL = "INSERT INTO books (book_id, title, is_available) VALUES (?, ?, ?)"
        val insertBookStatement: PreparedStatement = connection.prepareStatement(insertBookSQL)
        insertBookStatement.setInt(1, bookId)
        insertBookStatement.setString(2, title)
        insertBookStatement.setBoolean(3, true)
        insertBookStatement.executeUpdate()
        books += (bookId -> new Book(bookId, title))
        s"Book '$title' added with ID '$bookId'."
      } catch {
        case e: Exception =>
          s"Error adding book: ${e.getMessage}"
      }
    } else {
      s"Book with ID '$bookId' already exists."
    }
  }
  def generateTransactionReport(): String = {
    val query =
      """
        |SELECT * FROM transactions
        |JOIN users ON transactions.user_id = users.user_id
        |JOIN books ON transactions.book_id = books.book_id;
      """.stripMargin

    val resultSet: ResultSet = connection.createStatement().executeQuery(query)
    val transactions = new ArrayBuffer[Transaction]()

    while (resultSet.next()) {
      val user = new User(resultSet.getInt("user_id"), resultSet.getString("name"))
      val book = new Book(resultSet.getInt("book_id"), resultSet.getString("title"))

      transactions += new Transaction(user, book, resultSet.getString("action"), resultSet.getString("timestamp"))
    }

    transactions.map(_.toString).mkString("\n")
  }

  def generateUserReportById(targetUserId: Int): ArrayBuffer[Transaction] = {
    val query =
      s"""
         |SELECT * FROM transactions
         |JOIN users ON transactions.user_id = users.user_id
         |JOIN books ON transactions.book_id = books.book_id
         |WHERE users.user_id = $targetUserId;
       """.stripMargin

    val resultSet: ResultSet = connection.createStatement().executeQuery(query)
    val transactions = new ArrayBuffer[Transaction]()

    while (resultSet.next()) {
      val user = new User(resultSet.getInt("user_id"), resultSet.getString("name"))
      val book = new Book(resultSet.getInt("book_id"), resultSet.getString("title"))

      transactions += new Transaction(user, book, resultSet.getString("action"), resultSet.getString("timestamp"))
    }

    transactions
  }

  def displayRegisteredUsers(): String = {
    val query = "SELECT * FROM users;"
    val resultSet: ResultSet = connection.createStatement().executeQuery(query)

    val usersInfo = new ArrayBuffer[String]()
    while (resultSet.next()) {
      val userId = resultSet.getInt("user_id")
      val userName = resultSet.getString("name")
      usersInfo += s"ID: $userId, Name: $userName"
    }

    usersInfo.mkString("\n")
  }

  def checkOutBook(user: User, book: Book): String = {
    if (book.isAvailable) {
      try {
        // Insert the new transaction into the 'transactions' table
        val insertTransactionSQL =
          "INSERT INTO transactions (user_id, book_id, action, timestamp) VALUES (?, ?, ?, CURRENT_TIMESTAMP)"
        val insertTransactionStatement: PreparedStatement = connection.prepareStatement(insertTransactionSQL)
        insertTransactionStatement.setInt(1, user.userId)
        insertTransactionStatement.setInt(2, book.bookId)
        insertTransactionStatement.setString(3, "CHECKOUT")
        insertTransactionStatement.executeUpdate()

        // Update the local 'transactions' array
        transactions += Transaction(user, book, "CHECKOUT")

        // Update the 'isAvailable' status of the book
        val updateBookStatusSQL = "UPDATE books SET is_available = 0 WHERE book_id = ?"
        val updateBookStatusStatement: PreparedStatement = connection.prepareStatement(updateBookStatusSQL)
        updateBookStatusStatement.setInt(1, book.bookId)
        updateBookStatusStatement.executeUpdate()

        s"Book '${book.title}' checked out by user '${user.name}'."
      } catch {
        case e: Exception =>
          s"Error checking out book: ${e.getMessage}"
      }
    } else {
      s"Book '${book.title}' is not available."
    }
  }

  def returnBook(user: User, book: Book): String = {
    val transactionQuery =
      """
        |SELECT * FROM transactions
        |WHERE user_id = ? AND book_id = ? AND action = 'CHECKOUT';
      """.stripMargin

    val transactionStatement: PreparedStatement = connection.prepareStatement(transactionQuery)
    transactionStatement.setInt(1, user.userId)
    transactionStatement.setInt(2, book.bookId)

    val resultSet: ResultSet = transactionStatement.executeQuery()

    if (resultSet.next()) {
      val updateBookQuery = "UPDATE books SET is_available = ? WHERE book_id = ?;"
      val insertTransactionQuery =
        "INSERT INTO transactions (user_id, book_id, action, timestamp) VALUES (?, ?, ?, CURRENT_TIMESTAMP);"

      val updateBookStatement: PreparedStatement = connection.prepareStatement(updateBookQuery)
      updateBookStatement.setBoolean(1, true)
      updateBookStatement.setInt(2, book.bookId)
      updateBookStatement.executeUpdate()

      val insertTransactionStatement: PreparedStatement = connection.prepareStatement(insertTransactionQuery)
      insertTransactionStatement.setInt(1, user.userId)
      insertTransactionStatement.setInt(2, book.bookId)
      insertTransactionStatement.setString(3, "RETURN")
      insertTransactionStatement.executeUpdate()

      s"Book '${book.title}' returned by user '${user.name}'."
    } else {
      s"User '${user.name}' did not check out the book '${book.title}'."
    }
  }
  sys.addShutdownHook {
    connection.close()
  }
  private def getCurrentTimestamp: String = {
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
  }
}