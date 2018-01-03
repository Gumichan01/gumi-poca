

object Test {
  def main(args: Array[String]) : Unit = {
    var q1 = "Result for 1+1:";
    var poly = "0+1=1; 1+1=2";
    var ans1 = new TextAnswer("2");
    var ans2 = new TextAnswer("3");
    var ans3 = new TextAnswer("4");
    var ans4 = new TextAnswer("5");
    var prof = new Professor("Jean", "Pierre");
    var student1 = new Student("Michael","tata");
    var student2 = new Student("Michael", "toto");
    
    var listAnswer: List[Answer] = List(ans1, ans2, ans3, ans4);
    var listGoodAnswer: List[Answer] = List(ans1);
    var question = new MultipleChoiceQuestion(q1, listAnswer, listGoodAnswer);
    var survey = new Survey(List(question));
    var cours = new Course("Math", List(survey), poly, prof.getID, List(student1.getID,student2.getID));
   
    BD.addStudent(student1);
    BD.addStudent(student2);
    BD.addProf(prof);
    BD.addSurvey(survey);
    BD.addCourse(cours);
    
    println("------------------Information générale----------------------");
    println("List student:")
    BD.displayListStudent();
    println("List professeur:")
    BD.displayListProf();
    println("List des cours:")
    BD.displayListCourse();
    println("List des questions")
    BD.displaySurveyforCourse(cours);
    println("Poly")
    BD.displayPolyforCourse(cours)
    println("------------------------------------------------------------");
    prof.interfaceProf();
    student1.interfaceStudent();
    
  }
}