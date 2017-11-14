/*
    Question.scala: related to the question
*/

abstract class Question(sentence: String) {

    def text : String = sentence;
    def isGoodAnswer (a: List[Answer]) : Boolean
}
case class MultipleChoiceQuestion(sentence: String, answerl: List[Answer], ganswer: List[Answer])
    extends Question(sentence) {

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

        val l = List(new TextAnswer("yes",0), new TextAnswer("no",0));
        val g = new TextAnswer("yes",0);
        val q = new MultipleChoiceQuestion("Are you stupid?", l, g.toList);
        println(q.text);
        println("expected false; got: " + (q isGoodAnswer (new TextAnswer("y", 0)).toList));
        println("expected true; got: " + (q isGoodAnswer g.toList));
    }
}
