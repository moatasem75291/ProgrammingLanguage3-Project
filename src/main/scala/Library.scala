import scala.collection.mutable.ArrayBuffer
import java.io.{File, PrintWriter}
import scala.io.Source.fromFile
import java.text.SimpleDateFormat
import java.util.Date
import scala.util.Try

class Library {
  private var users = Map[Int, User]()
  private var books = Map[Int, Book]()
  private val transactions = ArrayBuffer[Transaction]()
  private val USERS_PATH: String = "C:\\Users\\MBR\\IdeaProjects\\LibraryManagementSystem\\src\\main\\scala\\User.txt"
  private val BOOKS_PATH: String = "C:\\Users\\MBR\\IdeaProjects\\LibraryManagementSystem\\src\\main\\scala\\Book.txt"
  private val TRANSACTION_PATH: String = "C:\\Users\\MBR\\IdeaProjects\\LibraryManagementSystem\\src\\main\\scala\\Transaction.txt"

  def getUsers: Map[Int, User] = users
  def getBooks: Map[Int, Book] = books
  def userExists(userId: Int): Boolean = users.contains(userId)
  def bookExists(bookId: Int): Boolean = books.contains(bookId)

  def loadFromFile(): Unit = {
    loadUsersFromFile(USERS_PATH)
    loadBooksFromFile(BOOKS_PATH)
    loadTransactionsFromFile(TRANSACTION_PATH)
  }

  def saveToFile(): Unit = {
    saveUsersToFile(USERS_PATH)
    saveBooksToFile(BOOKS_PATH)
    saveTransactionsToFile(TRANSACTION_PATH)
  }

  private def loadUsersFromFile(filePath: String): Unit = {
    val file = new File(filePath)
    if (file.exists()) {
      val lines = fromFile(file).getLines()
      lines.foreach { line =>
        val parts = line.split("\\|")
        Try(parts(1).toInt).foreach(userId => users += (userId -> new User(userId, parts(2))))
      }
    } else {
      println("File Not Found!!")
    }
  }

  private def loadBooksFromFile(filePath: String): Unit = {
    val file = new File(filePath)
    if (file.exists()) {
      val lines = fromFile(file).getLines()
      lines.foreach { line =>
        val parts = line.split("\\|")
        Try(parts(1).toInt).foreach(bookId => books += (bookId -> new Book(bookId, parts(2), parts(3).toBoolean)))
      }
    } else {
      println("File Not Found!!")
    }
  }

  private def loadTransactionsFromFile(filePath: String): Unit = {
    val file = new File(filePath)
    if (file.exists()) {
      val lines = fromFile(file).getLines()
      lines.foreach { line =>
        val parts = line.split("\\|")

        if (parts.length >= 5) {
          val userId = Try(parts(1).toInt).getOrElse(0)
          val bookId = Try(parts(2).toInt).getOrElse(0)
          val timestamp = parts(4)

          if (users.contains(userId) && books.contains(bookId)) {
            transactions += new Transaction(users(userId), books(bookId), parts(3), timestamp)
          } else {
            println(s"Skipping transaction: User with ID $userId or Book with ID $bookId not found.")
          }
        } else {
          println(s"Skipping malformed transaction line: $line")
        }
      }
    } else {
      println("File Not Found!!")
    }
  }

  private def saveUsersToFile(filePath: String): Unit = {
    val writer = new PrintWriter(new File(filePath))
    users.values.foreach(user => writer.println(user.toString))
    writer.close()
  }

  private def saveBooksToFile(filePath: String): Unit = {
    val writer = new PrintWriter(new File(filePath))
    books.values.foreach(book => writer.println(book.toString))
    writer.close()
  }

  private def saveTransactionsToFile(filePath: String): Unit = {
    val writer = new PrintWriter(new File(filePath))
    transactions.foreach(transaction => writer.println(transaction.toString))
    writer.close()
  }

  def registerUser(userId: Int, name: String): String = {
    if (!userExists(userId)) {
      val newUser = new User(userId, name)
      users += (userId -> newUser)
      s"User '${newUser.name}' registered with ID '$userId'."
    } else {
      s"User with ID '$userId' already exists."
    }
  }

  def addBook(bookId: Int, title: String): String = {
    if (!bookExists(bookId)) {
      val newBook = new Book(bookId, title)
      books += (bookId -> newBook)
      s"Book '${newBook.title}' added with ID '$bookId'."
    } else {
      s"Book with ID '$bookId' already exists."
    }
  }

  def checkOutBook(user: User, book: Book): String = {
    if (book.isAvailable) {
      book.isAvailable = false
      transactions += Transaction(user, book, "CHECKOUT")
      s"Book '${book.title}' checked out by user '${user.name}'."
    } else {
      s"Book '${book.title}' is not available."
    }
  }

  def returnBook(user: User, book: Book): String = {
    if (userExists(user.userId) && bookExists(book.bookId)) {
      val transaction = transactions.find(t =>
        t.user.userId == user.userId &&
          t.book.bookId == book.bookId &&
          t.action == "CHECKOUT"
      )

      transaction match {
        case Some(checkoutTransaction) =>
          book.isAvailable = true
          transactions += new Transaction(user, book, "RETURN", getCurrentTimestamp)
          s"Book '${book.title}' returned by user '${user.name}'."
        case None =>
          s"User '${user.name}' did not check out the book '${book.title}'."
      }
    } else {
      "Invalid user ID or book ID."
    }
  }

  private def getCurrentTimestamp: String = {
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
  }

  def generateTransactionReport(): String = {
    transactions.map(_.toString).mkString("\n")
  }

  def generateUserReportById(targetUserId: Int): ArrayBuffer[Transaction] = {
    transactions.filter(transaction => transaction.user.userId == targetUserId)
  }


  def displayRegisteredUsers(): String = {
    users.values.map(user => s"ID: ${user.userId}, Name: ${user.name}").mkString("\n")
  }
}
