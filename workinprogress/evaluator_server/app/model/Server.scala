package model

class Server {
  var listUsers: List[TestUser] = Nil

  def addUser(username: String, password: String, role: String) : Boolean = {
    // Plus tard ici matcher sur le role !
    // Pour déterminer si c'est un teacher ou un student
    // Egalement verifier si l'username n'existe pas
    val user = new TestUser(username, password, role)
    listUsers = user :: listUsers
    true
  }

  def userLogIn(username: String, password: String) : Boolean = {
    // Ici retourner true ou false suivant si le password est correct
    true
  }
}
