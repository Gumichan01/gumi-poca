package model

class AnswerSheet(ids: Int) {

    private var answers : List[(Answer, Int)] = List()

    // Constructor

    def addAnswer(a : Answer, qid: Int) : Unit = {
        answers = answers ++: List((a, qid))
    }
    def getAnswers = answers
}
