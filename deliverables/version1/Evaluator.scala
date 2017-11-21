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
                case 2 => { val (g,t) = quiz; println("\n" + g + "/" + t + "\n") }
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
            questions    = List()

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
            questions = questions ++: List[MultipleChoiceQuestion](quest)
            println("Do want to write another question (y|n)?")

        } while(StdIn.readLine() == "y")

        println("Generate the survey")
        val s : Survey = new MCSurvey(questions)
        lsurvey = lsurvey ++: List[Survey](s)

        return true
    }

    def quiz : (Int,Int) = {

        return (0,0)
    }
}
