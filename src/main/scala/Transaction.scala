import java.text.SimpleDateFormat
import java.util.Date

class Transaction(val user: User, val book: Book, val action: String, val timestamp: String) {
  override def toString: String = s"T|${user.userId}|${book.bookId}|$action|$timestamp"
}



object Transaction {
  def apply(user: User, book: Book, action: String): Transaction = {
    val timestamp = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date())
    new Transaction(user, book, action, timestamp)
  }
}
