
/*
  Questionnaire
*/

package model

import java.io.{BufferedWriter, File, FileWriter}
import sys.process._

object Survey {
    private var psid: Int = 0
    private def getId: Int = { psid += 1; psid }
}

trait WithCheckableQA {

    def check(answer_sh: AnswerSheet): Int
}

abstract class Survey(questionl: List[Question]) {

    if (questionl.isEmpty)
        throw new IllegalArgumentException("A survey cannot be empty")

    private val ids = Survey.getId
    def id : Int = ids
    def numberOfQuestions: Int = questionl.size

    override def toString: String = {
        "Survey with id = " + id
    }
}
/// MCSurvey is not supposed to exist anymore, but I must not remove it (too late!)
case class MCSurvey(questionl: List[MultipleChoiceQuestion])
    extends Survey(questionl) with WithCheckableQA {

    // Private
    protected def getQuestion(ql: List[MultipleChoiceQuestion], id: Int): MultipleChoiceQuestion = {

        if (id < 0)
            throw new IllegalArgumentException("A survey cannot be empty")

        ql match {
            case Nil    => throw new Exception("hello")
            case h :: q => { if (h.id == id) h else getQuestion(q, id) }
        }
    }

    // Public
    def check(answer_sh: AnswerSheet): Int = {

        var acc = 0
        check_(answer_sh.getAnswers, acc)
    }

    private def check_(answerl: List[(Answer, Int)], acc: Int): Int = {

        val g = acc

        answerl match {

            case (a, i) :: q => {

                val question = questionl.filter(_.id == i).head
                check_(q, (if (question isGoodAnswer List(a)) (g + 1) else acc ))
            }

            case _ => acc
        } // match
    }
}
// Survey that can get any type of question
case class GSurvey(questionl: List[Question])
    extends Survey(questionl) with WithCheckableQA {

    protected def getQuestion(ql: List[Question], id: Int): Question = {

        if (id < 0)
            throw new IllegalArgumentException("A survey cannot be empty")

        ql match {
            case Nil    => throw new Exception("Not found question")
            case h :: q => { if (h.id == id) h else getQuestion(q, id) }
        }
    }

    def check(answer_sh: AnswerSheet) = {

        val acc = 0
        check_(answer_sh, answer_sh.getAnswers, acc)
    }

    // dirty code
    private def check_(ansh: AnswerSheet, answerl: List[(Answer, Int)], acc: Int): Int = {

        val v = acc

        answerl match {

            case (a, i) :: q => {

                val question = getQuestion(questionl, i)

                question match {
                    case mcq: MultipleChoiceQuestion =>
                        check_(ansh, q, (if (mcq isGoodAnswer List(a)) (v + 1) else acc))

                    case srcq: CodeSubmissionQuestion =>
                        check_( ansh, q, ( v + ( checkSourceCode(srcq, ansh) ) ) )
                }
            }

            case _ => acc
        } // match
    }

    private def checkSourceCode(q : CodeSubmissionQuestion,
                                answer_sh : AnswerSheet) : Int = {

        var score = 0

        for (expectation <- q.ganswer) {
            val file = new File("tmp/" + "sourceCodeQuestion" + id + ".py")
            val bw = new BufferedWriter(new FileWriter(file))

            var fileContent = "import sys\n\n"

            val userAnswer = answer_sh.getAnswers.filter(_._2 == q.id)

            fileContent += userAnswer.head._1.asInstanceOf[CodeAnswer].sourceCode
            fileContent += "\n\n"

            fileContent += "sys.exit("+ q.functionName + "(" + expectation._1 + "))"

            bw.write(fileContent)
            bw.close()

            val resultCode = "python " + file.getAbsolutePath() !
            val resultFormat = "python " + file.getAbsolutePath() lineStream_!

            if (resultFormat.size > 0) {
                println("ERROR MAYBE NO ?")
            }

            if (resultCode.toString == expectation._2) {
                score += 1
            }
        }
        // Return the score
        score
    }
}

case class CodeSurvey(questionl: List[SourceCodeQuestion]) extends Survey(questionl) with WithCheckableQA {
    override def check(answer_sh: AnswerSheet): Int = {
        var score = 0

        for (q <- questionl) {
            for (expectation <- q.ganswer) {
                val file = new File("tmp/" + "sourceCodeQuestion" + id + ".py")
                val bw = new BufferedWriter(new FileWriter(file))

                var fileContent = "import sys\n\n"
                val userAnswer = answer_sh.getAnswers.filter(_._2 == q.id)

                fileContent += userAnswer.head._1.asInstanceOf[CodeAnswer].sourceCode
                fileContent += "\n\n"

                fileContent += "sys.exit("+ q.functionName + "(" + expectation._1 + "))"

                bw.write(fileContent)
                bw.close()

                val resultCode = "python " + file.getAbsolutePath() !
                val resultFormat = "python " + file.getAbsolutePath() lineStream_!

                if (resultFormat.size > 0) {
                    println("ERROR MAYBE NO ?")
                }

                if (resultCode.toString == expectation._2) {
                    score += 1
                }
            }
        }

        score
    }
}
/*
object SMain {

    def main(args: Array[String]): Unit = {

        // TODO: Test it
        var q1 = "Result for 1+1:";
        var poly = "0+1=1; 1+1=2";
        var ans1 = new TextAnswer("2");
        var ans2 = new TextAnswer("3");
        var ans3 = new TextAnswer("4");
        var ans4 = new TextAnswer("5");
        var ans5 = new MediaAnswer(new SourceCode("24"));
        // var prof = new Professor("Jean", "Pierre");
        // var student = new Student("Michael","Fail");

        var listAnswer: List[Answer] = List(ans1, ans2, ans3, ans4);
        var listGoodAnswer: List[Answer] = List(ans1);
        var question = new MultipleChoiceQuestion(q1, listAnswer, listGoodAnswer);

        val tcase = new TestCase {

            type Input = Int
            type Expected = Int

            def input: Input = 4
            def expected: Expected = 24
        }
        var srcquestion = new CodeSubmissionQuestion(q1, List(tcase));

        val s1 = new MCSurvey(List(question))
        val s2 = new MCSurvey(List(question))

        val l = List(srcquestion) ++: List(question)
        val s3 = new GSurvey(l)
        val sh = new AnswerSheet(3)

        sh addAnswer (ans1, question.id)
        sh addAnswer (ans5, srcquestion.id)

        println(s1)
        println(s2)
        println("check: " + s3.check(sh))
        println(s3)
    }

}
*/
