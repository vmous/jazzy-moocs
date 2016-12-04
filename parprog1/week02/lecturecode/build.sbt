scalaVersion := "2.11.5"

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-optimise",
  "-Yinline-warnings"
)

fork := true

javaOptions += "-Xmx3G"

parallelExecution in Test := false

libraryDependencies ++= Seq (
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  //"junit" % "junit" % "4.10" % "test",
  "com.novocode" % "junit-interface" % "0.8" % "test->default",
  "com.storm-enroute" %% "scalameter-core" % "0.6",
  "org.scalacheck" %% "scalacheck" % "1.12.1",
  "com.storm-enroute" %% "scalameter" % "0.6" % "test")

/**
 * Force sbt to use scala 2.11.5,
 * otherwise, some dependence will upgrade scala version to 2.11.7
 * in which `sort1` does not exist
 */
dependencyOverrides += "org.scala-lang" % "scala-library" % scalaVersion.value
