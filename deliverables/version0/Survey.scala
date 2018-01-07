
/*
  Questionnaire
*/

object Sid {
    private var psid : Int = 0
    val _id : Int = { psid = psid + 1; psid}
}

class Survey(questionl: List[Question]) {
    var ql = questionl;
    if(questionl.isEmpty)
        throw new IllegalArgumentException("A survey cannot be empty")

    private val ids = Sid._id
    def id : Int = ids

    def getQuestion(ql : List[Question], id: Int) : Question = {

        if(id < 0)
            throw new IllegalArgumentException("A survey cannot be empty")

        ql match {
            case Nil  => throw new Exception("hello")
            case h::q => { if (h.id == id) h else getQuestion(q,id) }
        }
    }
    
    def addQuestion(q: Question)={
      ql = q::ql
    }
    
    def displayQuestion(){
     ql.foreach(_.displayQuestion());
    }
    

    // I am still thinking about how I should define and implement it
    // Return the number of good answers
    def check(answer_sh: AnswerSheet): (Int,Int) = {

        var res = (0,0);    // (good_answers, question_to_check_manually)
        check_(answer_sh.getAnswers, res)
    }

    private def check_(answerl : List[(Answer, Int)], acc : (Int,Int)) : (Int,Int) = {

        val (g, nb) = acc

        answerl match {

            case (a, i)::q => {

                val question = getQuestion(questionl, i)

                question match {

                    case MultipleChoiceQuestion(_,_,_) => {

                        check_(q, (if (question isGoodAnswer List(a)) (g + 1, nb) else acc ))
                    }

                    case _ => check_(q, (g, nb + 1))
                }   //match
            }   // match

            case _  => acc
        }
    }
}

/*object SMain{

    def main(args: Array[String]) : Unit = {

        // TODO: Test it
    }

}*/
