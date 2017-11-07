/*
    Question.scala: related to the question
*/

abstract class Question(sentence: String) {

    def text : String = sentence;
    def isGoodAnswer (a: Answer) : Boolean
}
case class MultipleChoiceQuestion(sentence: String, answerl: List[Answer], ganswer: Answer)
    extends Question(sentence) {

    // Constructor
    if(!(answerl exists (_ == ganswer)))
        throw new IllegalArgumentException("The good answer is not in the list of answers");

    // Methods
    def isGoodAnswer (a: Answer) : Boolean = a == ganswer
}

object QMain {

    def main(args: Array[String]) : Unit = {

        val l = List(new TextAnswer("yes"), new TextAnswer("no"));
        val g = new TextAnswer("yes");
        val q = new MultipleChoiceQuestion("Are you stupid?", l, g);
        println(q.text);
        println("expected false; got: " + (q isGoodAnswer (new TextAnswer("y"))));
        println("expected true; got: " + (q isGoodAnswer g));
    }
}
