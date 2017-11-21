import scala.io.StdIn;


object Qevaluator {

    var lsurvey : List[Survey] = List()

    def main(args: Array[String]): Unit = {

        var go = true
        println("Evaluator 1.0")

        while(go) {
            println("What do you want to do?")
            println("1: Create a survey")
            println("2: Quiz")
            println("3: Exit")

            val n = StdIn.readInt()

            n match {
                case 1 => createSurvey
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

        println("Create multiple choice question")
        var answers      : List[Answer]   = List()
        var good_answers : List[Answer]   = List()
        var questions    : List[MultipleChoiceQuestion] = List()

        do {
            answers      = List()
            good_answers = List()

            println("Write the first question:")
            val q = StdIn.readLine()

            println("Write the possible answers (separated by '|')")
            val array = StdIn.readLine().split('|')

            var i = 0
            for(a <- array) {
                i = i + 1
                println(i + ": " + a)
                answers = answers ++: List[Answer](new TextAnswer(a))
            }

            println("What is/are the good answer(s) (if there are several answers write their id  with '|' as a separator)?")
            val arint = StdIn.readLine().split('|')

            for(ga <- arint) {
                good_answers = good_answers ++: List[Answer](answers(ga.toInt - 1))
            }

            println("Generate the question")
            val quest = new MultipleChoiceQuestion(q, answers, good_answers)
            questions = questions :+ quest
            println("Do want to write another question (y|n)?")
        } while(StdIn.readLine() == "y")

        println("Generate the survey")
        val s : Survey = new MCSurvey(questions)
        lsurvey = lsurvey ++: List[Survey](s)
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
        println("Question: " + question.text)

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
