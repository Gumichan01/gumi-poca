/*
* Test pour les classes
*/

package model

object Test {
  def main(args: Array[String]) : Unit = {
    var q1 = "Result for 1+1:";
    var poly = "0+1=1; 1+1=2";
    var ans1 = new TextAnswer("2");
    var ans2 = new TextAnswer("3");
    var ans3 = new TextAnswer("4");
    var ans4 = new TextAnswer("5");
    var prof = new Professor("Jean", "Pierre", "test");
    var student = new Student("Michael","Fail", "test2");

    var listAnswer: List[Answer] = List(ans1, ans2, ans3, ans4);
    var listGoodAnswer: List[Answer] = List(ans1);
    var question = new MultipleChoiceQuestion(q1, listAnswer, listGoodAnswer);
    var survey = new MCSurvey(List(question));
    var cours = new Course("Math", List(survey), poly, prof.getID, List(student.getID));
    println(cours.toString());

    var sheet = new AnswerSheet(survey.id);
    sheet.addAnswer(ans1, question.id);
    println(survey.check(sheet));

    var sheet2 = new AnswerSheet(survey.id);
    sheet2.addAnswer(ans3, question.id);
    println(survey.check(sheet2));
  }
}
