organization := "zamblauskas"

name := "scala-csv-parser"

libraryDependencies ++= Seq(
  "net.sf.opencsv" %  "opencsv"       % "2.3",
  "org.scala-lang" %  "scala-reflect" % scalaVersion.value,
  "org.scalatest"  %% "scalatest"     % "3.0.0"              % Test
)

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard"
)

// Implicit conversions are needed for the nice functional syntax
wartremoverErrors in (Compile, compile) ++= Warts.allBut(Wart.ImplicitConversion)
// Too much false-positives in the macro code - better just disable it
wartremoverExcluded += baseDirectory.value / "src/main/scala/zamblauskas/csv/parser/ReadsMacro.scala"

enablePlugins(spray.boilerplate.BoilerplatePlugin)

licenses := ("MIT", url("https://opensource.org/licenses/MIT")) :: Nil
