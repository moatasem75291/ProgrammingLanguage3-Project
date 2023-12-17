//ThisBuild / version := "0.1.0-SNAPSHOT"
//
//ThisBuild / scalaVersion := "2.13.12"
//
//lazy val root = (project in file("."))
//  .settings(
//    name := "LibraryManagementSystem"
//  )
scalaVersion := "2.13.12"
libraryDependencies += "de.tu-darmstadt.stg" %% "rescala" % "0.33.0"
libraryDependencies += "org.scalafx" %% "scalafx" % "16.0.0-R25"
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.36.0.1"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.17"
