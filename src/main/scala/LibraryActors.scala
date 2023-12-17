import akka.actor.Actor

class LibraryActors extends Actor {
  private val library = new Library()
  def receive: Receive = {
    case msg: String => library.exitSystem()
  }
}