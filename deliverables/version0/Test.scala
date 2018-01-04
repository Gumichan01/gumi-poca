

object Test {
  def main(args: Array[String]) : Unit = {
    var q1 = "Result for 1+1:";
    var q2 = "WTF stand for?"
    
    var poly1 = "0+1=1; 1+1=2";
    var poly2 = "English Grammar";
    
    
    var ans1 = new TextAnswer("2");
    var ans2 = new TextAnswer("3");
    var ans3 = new TextAnswer("4");
    var ans4 = new TextAnswer("5");
    
    var ans11 = new TextAnswer("What the hell");
    var ans21 = new TextAnswer("What the fuck");
    
    var prof1 = new Professor("Jean", "Pierre");
    var prof2 = new Professor("Ludo","Watson");
    
    var student1 = new Student("Michael","tata");
    var student2 = new Student("Michael", "toto");
    var student3 = new Student("Michael","titi");
    
    var listAnswer1: List[Answer] = List(ans1, ans2, ans3, ans4);
    var listGoodAnswer1: List[Answer] = List(ans1);
    var question1 = new MultipleChoiceQuestion(q1, listAnswer1, listGoodAnswer1);
    
    var listAnswer2: List[Answer] = List(ans11,ans21);
    var listGoodAnswer2: List[Answer] = List(ans21);
    var question2 = new MultipleChoiceQuestion(q2, listAnswer2, listGoodAnswer2);
    
    var survey1 = new Survey(List(question1));
    var survey2 = new Survey(List(question2));
    
    var cours1 = new Course("Math", List(survey1), poly1, prof1.getID, List(student1.getID,student2.getID));
    var cours2 = new Course("English", List(survey2), poly2, prof2.getID, List(student1.getID, student3.getID));
   
    BD.addStudent(student1);
    BD.addStudent(student2);
    BD.addStudent(student3);
    BD.addProf(prof1);
    BD.addProf(prof2);
    BD.addSurvey(survey1);
    BD.addSurvey(survey2);
    BD.addCourse(cours1);
    BD.addCourse(cours2);
    
    println("------------------Information générale----------------------");
    println("List student:")
    BD.displayListStudent();
    println("List professeur:")
    BD.displayListProf();
    println("List des cours:")
    BD.displayListCourse();
    println("List des questions")
    BD.displaySurveyforCourse(cours1);
    BD.displaySurveyforCourse(cours2);
    println("Poly")
    BD.displayPolyforCourse(cours1)
    BD.displayPolyforCourse(cours2)
    println("------------------------------------------------------------");
    //prof.interfaceProf();
    student1.interfaceStudent();
    
  }
}