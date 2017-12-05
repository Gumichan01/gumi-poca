
/*
    Answer.scala contains everything related to the answer
*/

// note: I didn't implemented MediaAnswer yet, I just want to test the base
trait ComparableAnswer[T]{

    def content : T
    def == (a: ComparableAnswer[T]) : Boolean
}

class Answer;
// Text answer
case class TextAnswer(text: String) extends Answer with ComparableAnswer[String] {

    def content = text
    def == (a: ComparableAnswer[String]) = content == a.content
}
// Media Answer
case class MediaAnswer extends Answer with ComparableAnswer[Media] {

}
