
/*
    Answer.scala contains everything related to the answer
*/

// note: I didn't implemented MediaAnswer yet, I just want to test the base
trait ComparableAnswer[T]{

    def content : T
    def == (a: ComparableAnswer[T]) : Boolean
}

class Answer {} // Answer will contain Media, don't remove it
case class TextAnswer(text: String) extends Answer with ComparableAnswer[String] {

    def content = text
    def == (a: ComparableAnswer[String]) = content == a.content
}
