class Book(val bookId: Int, val title: String, var isAvailable: Boolean = true) {
  override def toString: String = s"B|$bookId|$title|$isAvailable"
}