name := "evaluator_server"
 
version := "1.0" 
      
lazy val `evaluator_server` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.13.13"

libraryDependencies += "com.h2database" % "h2" % "1.4.192"
libraryDependencies ++= Seq( ehcache , jdbc, ws , specs2 % Test , guice )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  
