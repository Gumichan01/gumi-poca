import scala.collection.mutable.ListBuffer

class AnswerSheet {

    private var answers = new ListBuffer[(Answer, Int)]

    // Constructor

    def addAnswer(a : Answer, qid: Int) : Unit = {
        answers = answers ++: ListBuffer((a, qid))
    }
    def getAnswers = answers
}
