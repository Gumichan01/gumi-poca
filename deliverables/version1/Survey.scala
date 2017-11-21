
/*
  Questionnaire
*/

object Sid {
    private var psid : Int = 0
    val _id : Int = { psid = psid + 1; psid}
}

trait WithCheckableQA {

    def check(answer_sh: AnswerSheet) : Int
}

abstract class Survey(questionl: List[Question]) {

    if(questionl.isEmpty)
        throw new IllegalArgumentException("A survey cannot be empty")

    private val ids = Sid._id
    def id : Int = ids
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
    }

}
