/*
 * This class contains interface User and the implementation of student and professeur
 */
object userId {
  private var init: Int =0;
  val _id : Int = {init = init+1; init};
}
abstract class User (n: String, sN: String) {
  val id = userId._id;
  var name = n;
  var surName = sN;
  private var password = scala.util.Random.nextString(6);
  
  def getID: Int = id;
  def getName: String = name
  def getSurname: String = surName
  def setName(name: String) = {this.name = name}
  def getSurname(sname: String) = {this.surName = surName}
  def changePassword(newPsw: String)={password = newPsw;}
  def display()={println(id+ "-" + name+" " + surName)}
}

class Professor(n: String, sN: String ) extends User(n,sN) {
  def createPoly(content: String, c:Course)= c.addContent(content)
  def addQuestionnaire(s: Survey, c: Course) = c.addSurvey(s);
  def removeQuestionnaire(s: Survey, c:Course)= c.removeSurvey(s);
  
  def interfaceProf() {
    println("Bonjour professeur "+name);
    var c = BD.findCourse(BD.listC, id);
    var continue = true;
    var valide = true;
    print("Vous êtes tuteur du cours ");
    c.displayCourse();
    while(continue){
      if(valide){
        println("Liste des options pour vous: ");
        println("1. Ajouter poly pour le cours");
        println("2. Ajouter un questionnaire")
        println("3. Voir la liste des étudiants")
        println("4. Afficher le poly du cours");
        println("5. Afficher liste de survey");
        println("6. Exit");
      }
      
      print(">");
      var input = Console.readInt();
      
      if(input==1){
        valide = true;
        println("Tapez le contenu du cours que vous vouler ajouter: ");
        var chaine = Console.readLine();
        c.addContent("\n"+chaine);
      }
      else if(input==2){
        valide = true;
        print("Nombre de question vous voulez entrer: ");
        var nb = Console.readInt();
        var listQ = List[Question]();
        for(i<- 0 until nb){
          var lAns = List[Answer]();
          print("Question "+ (i+1)+ ":");
          var q = Console.readLine();
          println();
          print("Nombre de réponse: ");
          var nbR = Console.readInt();
          for(j <- 0 until nbR){
            print("Reponse"+ (j+1)+":");
            var a = Console.readLine();
            var ans = new TextAnswer(a);
            lAns = ans::lAns;
          }
          println();
          print("La bonne réponse: ");
          var gR = Console.readLine();
          var lGoodAns = List(new TextAnswer(gR));
          
          var quest = new MultipleChoiceQuestion(q, lAns, lGoodAns);
          listQ = quest::listQ;
        }
        var survey = new Survey(listQ);
        
        BD.addSurvey(survey);
        var c = BD.findCourse(BD.listC, id);
        c.addSurvey(survey);
        
      }
      else if(input==3){
        var listS = c.getListStudent();
        listS.foreach(i => BD.displayStudent(i))
      }
      else if(input==4){
        valide = true;
        var c2 = BD.findCourse(BD.listC, id);
        c2.displayPoly();
      }
      else if(input==5){
        valide = true;
        var c2 =BD.findCourse(BD.listC, id);
        BD.displaySurveyforCourse(c2);
      }
      else if(input==6){
        valide = true;
        continue = false;
        println("Vous avez bien déconnecté");
      }
      else {
        println("Votre choix est incorrect");
        valide = false;
      }
      
      Console.readLine();
    }
  }
}

class Student(n: String, sN: String) extends User(n,sN) {
  def registerACourse(c:Course)= c.addStudent(this.getID);
  def unregisterACourse(c:Course)= c.removeStudent(this.getID);
  def interfaceStudent(){
    println("Bonjour etudiant "+name);
    println("Votre id est "+id);
    var continue = true;
    var valide = true;
    while(continue){
      if(valide){
        println("Liste des options pour vous: ");
        println("1. Afficher liste des cours suivis");
        println("2. S'inscrire à un cours");
        println("3. Déinscrire un cours ");
        println("4. Répondre au questionnaire");
        println("5. Voir le process");
        println("6.Exit");
      }
      
      print(">");
      var input = Console.readInt();
      if(input==1){
        valide = true;
        println("Votre cours suivis: ");
        var listCours = BD.listC
        listCours.foreach(f => if(f.containStudent(id)) println(f.getNameCourse()))
      }
      else if(input==2){
        println("Liste des cours: ");
        BD.displayListCourse();
        print("Votre choix:");
        var choix = Console.readLine();
        if(!BD.listCourse().contains(choix)){
          println("Votre choix est invalide");
        }
        else {
          var course = BD.StringToCourse(choix);
          course.addStudent(id);
        }
      }
      else if(input==3){
        var lc = BD.displayListCourseForStudent(id);
        if(lc.isEmpty) println("Liste est vide ");
        println("Votre cours suivi: ");
        lc.foreach(f => println(f))
        print("Vous voulez déinscrire à quel cours: ")
        var choix = Console.readLine();
        if(!lc.contains(choix)){
          println("Votre choix est invalide!");
        }
        else {
          var course = BD.StringToCourse(choix);
          course.removeStudent(id);
        }
      }
      else if(input==6){
        valide = true;
        continue = false;
        println("Vous avez bien déconnecté");
      }
      else {
        println("Votre choix est incorrect");
        valide = false;
      }
      
    }
  }
  //def readPoly()
}