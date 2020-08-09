name := """hands-on-scalaz"""

version := "1.0"

scalaVersion := "2.12.12"

scalacOptions += "-Ypartial-unification"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.1.5"

libraryDependencies += "org.specs2" %% "specs2-core" % "3.8.3-scalaz-7.1" % "test"

libraryDependencies += "org.typelevel" %% "cats-core" % "1.6.0"
