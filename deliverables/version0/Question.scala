/*
    Question.scala: related to the question
*/

object Qid {
    private var pqid : Int = 0
    val _id : Int = { pqid = pqid + 1; pqid}
}

abstract class Question(sentence: String) {

    private val idq = Qid._id
    def text : String = sentence;
    def id : Int = idq
    def isGoodAnswer (a: List[Answer]) : Boolean
    def displayQuestion()
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

    def displayQuestion()= {
      println(sentence)
    }
    // Methods
    def isGoodAnswer (a: List[Answer]) : Boolean = a.toSet == ganswer.toSet
}

