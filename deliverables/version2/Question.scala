/*
    Question.scala: related to the question
*/

/// BUG it seems that the id of a question is not unique (to fix)
object Qid {
    private var pqid : Int = 0
    val _id : Int = { pqid = pqid + 1; pqid}
}

trait QuestionWithCheckablaAnswer {
    def isGoodAnswer (a: List[Answer]) : Boolean
}

abstract class Question(sentence: String) {

    private val idq = Qid._id
    def text : String = sentence;
    def id : Int = idq
}
case class MultipleChoiceQuestion(sentence: String, answerl: List[Answer], ganswer: List[Answer])
    extends Question(sentence) with QuestionWithCheckablaAnswer {

    // Constructor
    ganswer match {
        case List() => throw new IllegalArgumentException("No good answer has been given")
        case _  => {

            if (!ganswer.forall(answerl.contains))
                throw new IllegalArgumentException("The good answer is not in the list of answers")
        }
    }

    // Methods
    def isGoodAnswer (a: List[Answer]) : Boolean = a.toSet == ganswer.toSet
}

object QMain {

    def main(args: Array[String]) : Unit = {

        val l = List(new TextAnswer("yes"), new TextAnswer("no"));
        val g = new TextAnswer("yes");
        val q = new MultipleChoiceQuestion("Are you stupid?", l, List(g));
        println(q.text);
        println("expected false; got: " + (q isGoodAnswer List(new TextAnswer("y")) ) );
        println("expected true; got: " + (q isGoodAnswer List(g)));
    }
}
