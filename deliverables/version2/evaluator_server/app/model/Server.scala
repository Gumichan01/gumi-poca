package model

object Server {
  var listUsers: List[User] = Nil
  var listCourses: List[Course] = Nil
  var listSurveys: List[Survey] = Nil

  def addStartData: Unit = {
    val prof = new Professor("Gustavo", "Petri", "gpetri");
    val student1 = new Student("Jean", "Valjean", "hugo");
    val student2 = new Student("James", "Bond", "goldeneye");
    val student3 = new Student("Luke", "Skywalker", "force");
    val listStudents: List[Student] = student1 :: student3 :: Nil

    listUsers = prof :: student1 :: student2 :: student3 :: listUsers

    val ta11 = new TextAnswer("Scala")
    val ta12 = new TextAnswer("Python")
    val ta13 = new TextAnswer("C++")
    val q1 = new MultipleChoiceQuestion("En quel langage evaluator est-il crée ?", (ta11::ta12::ta13::Nil), (ta11::Nil))

    val ta21 = new TextAnswer("alloc")
    val ta22 = new TextAnswer("malloc")
    val ta23 = new TextAnswer("new")
    val q2 = new MultipleChoiceQuestion("Comment fait-on une allocation dynamique en C++ ?", (ta21::ta22::ta23::Nil), (ta23::Nil))

    val ta31 = new TextAnswer("Fonctionnel")
    val ta32 = new TextAnswer("Objet")
    val ta33 = new TextAnswer("Au typage faible")
    val q3 = new MultipleChoiceQuestion("Quelles sont les particularités de Scala ?", (ta31::ta32::ta33::Nil), (ta31::ta32::Nil))

    val s : Survey = new MCSurvey(q1::q2::q3::Nil)

    val course = new Course("POOCAv", (s :: Nil), "Cours de POO Concepts Avancés<br />Apprenons le Scala !", prof.getID, listStudents.map {_.getID})

    listCourses = course :: Nil
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

  def addSurvey(survey: Survey) : (Boolean,Int) = {
    listSurveys = survey :: listSurveys
    (true,survey.id)
  }

  def getSurvey(cid: Long, sid: Long) : Option[Survey] = {
    val course = getCourse(cid)
    course match {
      case Some(c) => {
        val fs = c.getSurveys.filter(_.id == sid)
        fs.size match {
          case 1 => Some(fs.head)
          case 0 => None
        }
      }
      case None => None
    }
  }

  def studentSuscribes(cid: Long, surname: String) : Boolean = {
    (userWithSurname(surname)) match {
      case Some(user) => {
        val course = listCourses.filter(_.getID == cid)
        if (course.size == 0) {
          false
        }
        else {
          course.head.addStudent(user.getID)
          true
        }
      }
      case None => false
    }
  }

  def evaluateAnswerSheet(survey: MCSurvey, sheet: AnswerSheet) : Int = {
    val score = survey.check(sheet)
    score
  }
}
