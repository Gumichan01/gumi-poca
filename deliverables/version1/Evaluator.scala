import scala.io.StdIn;


object Qevaluator {

    def main(args: Array[String]): Unit = {

        var go = true
        var lsurvey : List[Survey] = List()

        println("Evaluator 1.0")

        while(go) {
            println("What do you want to do?")
            println("1: Create a survey")
            println("2: Quiz")
            println("3: Exit")

            val n = StdIn.readInt()

            n match {
                case 1 => println("")
                case 2 => { quiz(lsurvey) match {
                        case None => println("Passing...")
                        case Some((g,t)) => println("Your score : " + g + "/" + t); 
                    }
                }
                     
                case 3 => go = false
                case _ => throw new IllegalArgumentException("invalid argument")
            }   // match
        }       // while
    }

    def createSurvey : Boolean = {
        return true
    }

    def quiz(ls: List[Survey]) : Option[(Int,Int)] = {
        if (ls.isEmpty) {
          println("No surveys, create one first!")
          return None
        }

        println("Which survey do you want to answer ?")

        ls.foreach(println)

        val choice = StdIn.readInt()

        try {
          ls(choice-1) match { 
            case mcs: MCSurvey => return _interactiveMCSurvey(mcs)
            case _ => println("Unhandled survey") 
          }
        }
        catch {
          case e: IndexOutOfBoundsException => println("Invalid argument")
        }

        return None
    }
    
    def _interactiveMCSurvey(s: MCSurvey) : Option[(Int, Int)] = {
      var sheet = new AnswerSheet(s.id)

      for (question <- s.questionl) {
        println("Question: question.text")
        
        var count = 0

        for (answer <- question.answerl) {
          answer match {
            case t: TextAnswer => {
              count += 1
              println(count + ") " + t.content)
            }
            case _ => "Not handled type"
          }
        }

        println("Your answer : ")
        val userInput = StdIn.readInt()
        val userAnswer = question.answerl(userInput-1)

        // We're forced for the moment because content
        // is available only on TextAnswer (for the moment!)

        userAnswer match {
          case t: TextAnswer => val ta = TextAnswer(t.content) ; sheet.addAnswer(ta, question.id)
          case _ => "Not handled type"
        }
      }

      return Some((s.check(sheet),s.questionl.length))
    }
}
