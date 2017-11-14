
/*
    Answer.scala contains everything related to the answer
*/

// note: I didn't implemented MediaAnswer yet, I just want to test the base
trait GenericAnswer[T]{

    def content : T
    def == (a: GenericAnswer[T]) : Boolean
}

class Answer(id_question : Int) {   // Answer will contain Media, don't remove it

    def id = id_question

    def toList() : List[Answer] = List(this)
}
case class TextAnswer(text: String, idq: Int) extends Answer(idq) with GenericAnswer[String] {

    def content = text
    def == (a: GenericAnswer[String]) = content == a.content
}
