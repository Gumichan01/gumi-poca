/*
 * A course contains a set of Survey
 */
object Cid{
    private var pcid : Int = 0;
    val _id : Int = { pcid = pcid + 1; pcid};
}
class Course(nom:String,survl: List[Survey], cont: String, idProf: Int, listStudent: List[Int]) {
  private val name = nom;
  private val idC = Cid._id;
  private var surveyl = survl;
  private var content =cont;
  private var idP = idProf;
  private var listS = listStudent;

  def getSurvey(surveyl: List[Survey], id: Int): Survey = {
    if(id<0){
      throw new IllegalArgumentException("Numbre of survey invalide");
    }
    surveyl match {
      case Nil => println("This course doesn't have question"); return null;
      case h::t => if(h.id==id) h else getSurvey(t,id);
    }
    return null;
  }
  
  def addSurvey(s: Survey) = {
    surveyl.+:(s);
  }
  def removeSurvey(s:Survey)={
    surveyl.filterNot(elem=> elem ==s);
  }
  def addStudent(i: Int)={
    listS.+:(i);
  }
  def removeStudent(i: Int)={
    listS.filterNot(elem=>elem==i);
  }
  def addContent(cont : String) ={
    content = cont;
  }
}