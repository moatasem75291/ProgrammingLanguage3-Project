import akka.actor.{ActorRef, ActorSystem, Props}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.TextInputDialog
object LibraryManagementGUI extends JFXApp {
  private val library = new Library()

  private val actorSystem: ActorSystem = ActorSystem("ActorSystem")
  private val actor: ActorRef = actorSystem.actorOf(Props[LibraryActors], "LibrarySystem")

  library.loadFromDataBase()

  private val resultTextArea = new TextArea()
  private val labeledChoiceBox = new Label("Choose an action:")
  private val choiceBox = new ChoiceBox[String](ObservableBuffer(
    "Register User",
    "Add Book",
    "Check Out Book",
    "Return Book",
    "Display Registered Users",
    "Display Books",
    "Generate Transaction Report",
    "Generate User Report",
    "Exit")
  )

  stage = new PrimaryStage {
    title = "Library Management System"
    scene = new Scene(800, 500) {
      resultTextArea.editable = false

      choiceBox.selectionModel().selectedItemProperty().addListener { (_, _, newValue) =>
        newValue match {

          case "Register User" =>
            val userIdDialog = new TextInputDialog(defaultValue = "0")
            userIdDialog.headerText = "Enter user ID:"
            userIdDialog.contentText = "User ID:"
            val userIdResult = userIdDialog.showAndWait()

            userIdResult match {
              case Some(userId) =>
                val userNameDialog = new TextInputDialog(defaultValue = "")
                userNameDialog.headerText = "Enter user name:"
                userNameDialog.contentText = "User Name:"
                val userNameResult = userNameDialog.showAndWait()

                userNameResult match {
                  case Some(userName) =>
                    val result = library.registerUser(userId.toInt, userName)
                    resultTextArea.text = result
                  case None =>
                    resultTextArea.text = "Registration canceled."
                }

              case None =>
                resultTextArea.text = "Registration canceled."
            }

          case "Add Book" =>
            val bookIdDialog = new TextInputDialog(defaultValue = "0")
            bookIdDialog.headerText = "Enter book ID:"
            bookIdDialog.contentText = "Book ID:"
            val bookIdResult = bookIdDialog.showAndWait()

            bookIdResult match {
              case Some(bookId) =>
                val bookTitleDialog = new TextInputDialog(defaultValue = "")
                bookTitleDialog.headerText = "Enter book title:"
                bookTitleDialog.contentText = "Book Title:"
                val bookTitleResult = bookTitleDialog.showAndWait()

                bookTitleResult match {
                  case Some(bookTitle) =>
                    val result = library.addBook(bookId.toInt, bookTitle)
                    resultTextArea.text = result
                  case None =>
                    resultTextArea.text = "Book addition canceled."
                }
              case None =>
                resultTextArea.text = "Book addition canceled."
            }

          case "Check Out Book" =>
            val userIdDialog = new TextInputDialog(defaultValue = "0")
            userIdDialog.headerText = "Enter user ID:"
            userIdDialog.contentText = "User ID:"
            val userIdResult = userIdDialog.showAndWait()

            userIdResult match {
              case Some(userId) =>
                val bookIdDialog = new TextInputDialog(defaultValue = "0")
                bookIdDialog.headerText = "Enter book ID:"
                bookIdDialog.contentText = "Book ID:"
                val bookIdResult = bookIdDialog.showAndWait()

                bookIdResult match {
                  case Some(bookId) =>
                    if (library.userExists(userId.toInt) && library.bookExists(bookId.toInt)) {
                      val user = library.getUsers(userId.toInt)
                      val book = library.getBooks(bookId.toInt)
                      val result = library.checkOutBook(user, book)
                      resultTextArea.text = result
                    } else {
                      resultTextArea.text = s"User with ID $userId or Book with ID $bookId not found."
                    }
                  case None =>
                    resultTextArea.text = "Check-in operation canceled."
                }

              case None =>
                resultTextArea.text = "Check-in operation canceled."
            }

          case "Return Book" =>
            val userIdDialog = new TextInputDialog(defaultValue = "0")
            userIdDialog.headerText = "Enter user ID:"
            userIdDialog.contentText = "User ID:"
            val userIdResult = userIdDialog.showAndWait()

            userIdResult match {
              case Some(userId) =>
                val bookIdDialog = new TextInputDialog(defaultValue = "0")
                bookIdDialog.headerText = "Enter book ID:"
                bookIdDialog.contentText = "Book ID:"
                val bookIdResult = bookIdDialog.showAndWait()

                bookIdResult match {
                  case Some(bookId) =>
                    if (library.userExists(userId.toInt) && library.bookExists(bookId.toInt)) {
                      val user = library.getUsers(userId.toInt)
                      val book = library.getBooks(bookId.toInt)
                      val result = library.returnBook(user, book)
                      resultTextArea.text = result
                    } else {
                      resultTextArea.text = s"User with ID $userId or Book with ID $bookId not found."
                    }
                  case None =>
                    resultTextArea.text = "Return operation canceled."
                }

              case None =>
                resultTextArea.text = "Return operation canceled."
            }

          case "Display Registered Users" =>
            val usersReport = library.displayRegisteredUsers()
            resultTextArea.text = usersReport

          case "Display Books" =>
            val booksReport = library.displayAllBooks()
            resultTextArea.text = booksReport

          case "Generate Transaction Report" =>
            val transactionsReport = library.generateTransactionReport()
            resultTextArea.text = transactionsReport

          case "Generate User Report" =>
            val userIdDialog = new TextInputDialog(defaultValue = "0")
            userIdDialog.headerText = "Enter user ID:"
            userIdDialog.contentText = "User ID:"
            val userIdResult = userIdDialog.showAndWait()

            userIdResult match {
              case Some(userId) =>
                val userReport = library.generateUserReportById(userId.toInt)
                if (userReport.nonEmpty) {
                  resultTextArea.text = userReport.mkString("\n")
                } else {
                  resultTextArea.text = "This user doesn't have transactions to display. Try another one."
                }

              case None =>
                resultTextArea.text = "Generate User Report canceled."
            }
          case "Exit" =>
            resultTextArea.text = "Exiting... Let's see you again :)"
            actor ! "Exit"

          case _ =>
            resultTextArea.text = "Invalid choice. Please choose a valid action."

        }

      }



      val vbox = new VBox(10,
        labeledChoiceBox,
        choiceBox,
        resultTextArea
      )
      root = new BorderPane {
        center = vbox
      }

    }
  }
}
