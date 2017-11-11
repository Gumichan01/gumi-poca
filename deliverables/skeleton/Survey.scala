
/*
  Questionnaire
*/

class Survey(questionl: List[Question]) {

    if(questionl.isEmpty)
        throw new IllegalArgumentException("A survey cannot be empty")

    // I am still thinking about how I should define and implement it
    // Return the number of good answers
    def check(answerl: List[Answer]): Int = {

        var good = 0
        check_(answerl, good)
    }

    private def check_(answerl : List[Answer], acc : Int) : Int = {

        answerl match {

            case h::q => check_(q, (if (questionl(h.id) isGoodAnswer h)  (acc + 1) else acc ) )
            case _    => acc
        }
    }
}

object SMain{

    def main(args: Array[String]) : Unit = {

        // TODO: Test it
    }

}
