package model

abstract class Media
case class SourceCode(text: String) extends Media {

    // Constructor
    def code = text

}
