/*
 * This class contains interface User and the implementation of student and professeur
 */

package model

object userId {
  private var init: Int =0;
  val _id : Int = {init = init+1; init};
}

abstract class User (n: String, sN: String, p: String) {
  private val id = userId._id;
  private var name = n;
  private var surName = sN;
  private var password = p;
  
  def getID: Int = id;
  def getName: String = name
  def getSurname: String = surName
  def getPassword: String = password
  def setName(name: String) = {this.name = name}
  def getSurname(sname: String) = {this.surName = surName}
  def setPassword(password: String) = {this.password = password}
}

class Professor(n: String, sN: String, p: String) extends User(n,sN,p) {
  def createPoly(content: String, c:Course)= c.addContent(content)
  def addQuestionnaire(s: Survey, c: Course) = c.addSurvey(s);
  def removeQuestionnaire(s: Survey, c:Course)= c.removeSurvey(s);
}

class Student(n: String, sN: String, p: String) extends User(n,sN,p) {
  def registerACourse(c:Course)= c.addStudent(this.getID);
  def unregisterACourse(c:Course)= c.removeStudent(this.getID);
}