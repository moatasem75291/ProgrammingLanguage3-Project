class User(val userId: Int, val name: String) {
  override def toString: String = s"U|$userId|$name"
}
