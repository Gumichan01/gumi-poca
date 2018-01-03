/*
    Question.scala: related to the question
*/

package model

object Qid {
    private var pqid: Int = 0
    val _id: Int = { pqid = pqid + 1; pqid }
}

trait QuestionWithCheckablaAnswer {
    def isGoodAnswer(a: List[Answer]): Boolean
}

trait QuestionWithMedia {
    def check(a: Answer): Boolean
}

// Class to instantiate with
abstract class TestCase {

    type Input
    type Expected

    def input: Input
    def expected: Expected
}

abstract class Question(sentence: String) {

    private val idq = Qid._id
    def text: String = sentence;
    def id: Int = idq
}
case class MultipleChoiceQuestion(sentence: String, answerl: List[Answer], ganswer: List[Answer])
    extends Question(sentence) with QuestionWithCheckablaAnswer {

    // Constructor
    ganswer match {
        case List() => throw new IllegalArgumentException("No good answer has been given")
        case _ => {

            if (!ganswer.forall(answerl.contains))
                throw new IllegalArgumentException("The good answer is not in the list of answers")
        }
    }

    // Methods
    def isGoodAnswer(a: List[Answer]): Boolean = a.toSet == ganswer.toSet
}
case class CodeSubmissionQuestion(sentence: String, tests: List[TestCase])
    extends Question(sentence) with QuestionWithMedia {

    def check(a: Answer) = true
}

object QMain {

    def main(args: Array[String]): Unit = {

        val l = List(new TextAnswer("yes"), new TextAnswer("no"));
        val g = new TextAnswer("yes");
        val q = new MultipleChoiceQuestion("Are you stupid?", l, List(g));
        println(q.text);
        println("expected false; got: " + (q isGoodAnswer List(new TextAnswer("y"))));
        println("expected true; got: " + (q isGoodAnswer List(g)));
    }
}
