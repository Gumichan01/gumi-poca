
/*
  Questionnaire
*/

package model

object Survey {
    private var psid : Int = 0
    private def getId : Int = { psid += 1; psid}
}

trait WithCheckableQA {

    def check(answer_sh: AnswerSheet) : Int
}

abstract class Survey(questionl: List[Question]) {

    if(questionl.isEmpty)
        throw new IllegalArgumentException("A survey cannot be empty")

    private val ids = Survey.getId
    def id : Int = ids

    override def toString : String = {
      "Survey with id = " + id
    }
}
case class MCSurvey(questionl: List[MultipleChoiceQuestion]) extends Survey(questionl) with WithCheckableQA {

    // Private
    protected def getQuestion(ql : List[MultipleChoiceQuestion], id: Int) : MultipleChoiceQuestion = {

        if(id < 0)
            throw new IllegalArgumentException("A survey cannot be empty")

        ql match {
            case Nil  => throw new Exception("hello")
            case h::q => { if (h.id == id) h else getQuestion(ql,id) }
        }
    }

    // Public
    def check(answer_sh: AnswerSheet) : Int = {

        var acc = 0
        check_(answer_sh.getAnswers, acc)
    }

    private def check_(answerl : List[(Answer, Int)], acc : Int) : Int = {

        val g = acc

        answerl match {

            case (a, i)::q => {

                val question = getQuestion(questionl, i)
                check_(q, (if (question isGoodAnswer List(a)) (g + 1) else acc ))
            }

            case _  => acc
        }   // match
    }
}

object SMain{

    def main(args: Array[String]) : Unit = {

        // TODO: Test it
        var q1 = "Result for 1+1:";
        var poly = "0+1=1; 1+1=2";
        var ans1 = new TextAnswer("2");
        var ans2 = new TextAnswer("3");
        var ans3 = new TextAnswer("4");
        var ans4 = new TextAnswer("5");
        // var prof = new Professor("Jean", "Pierre");
        // var student = new Student("Michael","Fail");

        var listAnswer: List[Answer] = List(ans1, ans2, ans3, ans4);
        var listGoodAnswer: List[Answer] = List(ans1);
        var question = new MultipleChoiceQuestion(q1, listAnswer, listGoodAnswer);

        val s1 = new MCSurvey(List(question))
        val s2 = new MCSurvey(List(question))
        println(s1)
        println(s2)
    }

}
