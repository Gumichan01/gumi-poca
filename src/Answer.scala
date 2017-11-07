
/*
    Answer.scala contains everything related to the answer
*/

// note: I didn't implemented MediaAnswer yet, I just want to test the base
trait GenericAnswer[T]{

    def content : T
    def == (a: GenericAnswer[T]) : Boolean
}

class Answer    // Answer will contain Media, don't remove it
case class TextAnswer(text: String) extends Answer with GenericAnswer[String] {

    def content = text
    def == (a: GenericAnswer[String]) = content == a.content
}
