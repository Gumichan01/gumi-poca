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
                case 2 => { quiz match {
                        case None => println("Auccune réponse :/ !.")
                        case Some((g,t)) => println(g+"/"+t); 
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

    def quiz : Option[(Int,Int)] = {


        return Some((0,0)) 
    }

}
