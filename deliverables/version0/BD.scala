object BD {
  var listS = List[Student]();
  var listP = List[Professor]();
  var listV = List[Survey]();
  var listC = List[Course]();
  
  def listIDStudent(): List[Int]={
    var ls = List()
    listS.foreach(_.getID::ls)
    ls
  }
  
  def listIDProf(): List[Int]={
    var lp = List()
    listP.foreach(_.getID::lp)
    lp
  }
  
  def listIDSurvey(): List[Int]={
    var ls = List()
    listV.foreach(_.id::ls)
    ls
  }
  
  def findCourse(lc: List[Course],idP: Int): Course = {
    lc match {
      case Nil => null
      case h::t => if(h.getProf()==idP) h.getCourse()
                   else findCourse(t, idP);
    }
  }
  
  def listCourse(): List[String]={
    var lc = List()
    listC.foreach(_.getNameCourse()::lc)
    lc
  }
  
  def addStudent(s:Student)={
    listS = s::listS
  }
  
  def addProf(p:Professor)={
    listP = p::listP
  }
  
  def addSurvey(s: Survey)={
    listV = s::listV
  }
  
  def addCourse(c:Course)={
    listC = c::listC
  }
  
  def displayListStudent()={
    listS.foreach(_.display())
  }
  
  def displayStudent(id: Int)={
    listS.foreach(f => if(f.getID ==id) println(f.name+ " "+f.surName))
  }
  def displayListProf()={
    listP.foreach(_.display())
  }
  
  def displayListCourse()={
    listC.foreach(_.displayCourse())
  }
  
  def displaySurveyforCourse(c:Course)={
    c.displaySurvey()
  }
  
  def displayPolyforCourse(c:Course)={
    c.displayPoly()
  }
  
  def CourseIsValide(c: String){
    
  }
  
}