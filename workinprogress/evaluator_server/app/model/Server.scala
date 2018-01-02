package model

object Server {
  var listUsers: List[User] = Nil
  var listCourses: List[Course] = Nil

  def addStartData: Unit = {
    var prof = new Professor("Gustavo", "Pietri", "gpietri");
    var student1 = new Student("Jean", "Valjean", "hugo");
    var student2 = new Student("James", "Bond", "goldeneye");
    var student3 = new Student("Luke", "Skywalker", "force");

    listUsers = prof :: student1 :: student2 :: student3 :: listUsers
  }

  def addUser(surname: String, name: String, password: String, role: String) : Boolean = {
    if (checkSurnameAvailable(surname) == false)
      false

    var user: User = null

    role match {
      case "professor" => user = new Professor(name, surname, password)
      case "student" => user = new Student(name, surname, password)
      case _ => false
    }

    listUsers = user :: listUsers
    true
  }

  def checkSurnameAvailable(surname: String) : Boolean = {
    userWithSurname(surname) != None
  }

  def userLogIn(surname: String, password: String) : Boolean = {
    val filteredSurnames = listUsers.filter(_.getSurname.equals(surname))
    if (filteredSurnames.size == 0) {
      return false
    }
    else {
      val foundUser = filteredSurnames.head
      return foundUser.getPassword.equals(password)
    }
  }

  def userWithSurname(surname: String) : Option[User] = {
    val filteredSurnames = listUsers.filter(_.getSurname.equals(surname))
    filteredSurnames.size match {
      case 1 => Some(filteredSurnames.head)
      case _ => None
    }
  }

  def addCourse(name: String, content: String, profId: Int) : (Boolean,Int) = {
    val course = new Course(name, Nil, content, profId, Nil)
    listCourses = course :: listCourses
    (true,course.getID)
  }

  def getCourse(cid: Long) : Option[Course] = {
    val filteredCourses = listCourses.filter(_.getID == cid)
    filteredCourses.size match {
      case 1 => Some(filteredCourses.head)
      case _ => None
    }
  }
}
