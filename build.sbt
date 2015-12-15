organization := "zamblauskas"

name := "scala-csv-parser"

scalaVersion := "2.11.7"

version := "0.6.0"

libraryDependencies ++= Seq(
  "org.scalaz"     %% "scalaz-core" % "7.1.5",
  "net.sf.opencsv" %  "opencsv"     % "2.3",
  "org.scalatest"  %% "scalatest"   % "2.2.5"  % "test"
)

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard"
)

// False positives:
// - Wart.Throw https://github.com/puffnfresh/wartremover/issues/182
// - Wart.IsInstanceOf & Wart.AsInstanceOf https://github.com/puffnfresh/wartremover/issues/152
wartremoverErrors in (Compile, compile) ++= Warts.allBut(Wart.Throw, Wart.IsInstanceOf, Wart.AsInstanceOf)
