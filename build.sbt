organization := "io.github.zamblauskas"

name := "scala-csv-parser"

libraryDependencies ++= Seq(
  "com.opencsv" %  "opencsv"       % "5.5.2",
  "org.scalatest"  %% "scalatest"  % "3.2.10"  % Test
)

scalaVersion := "3.0.0"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-Xfatal-warnings"
)

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