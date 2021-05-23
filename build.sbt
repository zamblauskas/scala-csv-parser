organization := "io.github.zamblauskas"

name := "scala-csv-parser"

libraryDependencies ++= Seq(
  "net.sf.opencsv" %  "opencsv"       % "2.3",
  "org.scala-lang" %  "scala-reflect" % scalaVersion.value,
  "org.scalatest"  %% "scalatest"     % "3.2.9"              % Test
)

val scala211 = "2.11.12"
val scala212 = "2.12.13"
val scala213 = "2.13.6"

scalaVersion := scala213
crossScalaVersions := Seq(scala211, scala212, scala213)

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-Xfatal-warnings",
  "-Xlint",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard"
)

// Too much false-positives in the macro code - better just disable it
wartremoverExcluded += baseDirectory.value / "src/main/scala/zamblauskas/csv/parser/ReadsMacro.scala"

enablePlugins(spray.boilerplate.BoilerplatePlugin)

licenses := ("MIT", url("https://opensource.org/licenses/MIT")) :: Nil
homepage := Some(url("https://github.com/zamblauskas/scala-csv-parser"))
developers := List(
  Developer(
    "contributors",
    "Contributors",
    "https://github.com/zamblauskas/scala-csv-parser/graphs/contributors",
    url("https://github.com/zamblauskas/scala-csv-parser/graphs/contributors")
  )
)

sonatypeCredentialHost := "s01.oss.sonatype.org"
sonatypeRepository := "https://s01.oss.sonatype.org/service/local"