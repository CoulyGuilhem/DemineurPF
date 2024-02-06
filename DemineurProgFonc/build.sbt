ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "DemineurProgFonc",
    libraryDependencies += "org.scalafx" %% "scalafx" % "16.0.0-R24",
    libraryDependencies += "org.openjfx" % "javafx-controls" % "17" classifier "win",
    libraryDependencies += "org.openjfx" % "javafx-graphics" % "17" classifier "win",
    libraryDependencies += "org.openjfx" % "javafx-media" % "17" classifier "win",
  )
