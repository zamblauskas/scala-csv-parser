organization := "zamblauskas"

name := "scala-csv-parser"

scalaVersion := "2.11.7"

version := "0.7.1"

libraryDependencies ++= Seq(
  "net.sf.opencsv" %  "opencsv"     % "2.3",
  "org.scalatest"  %% "scalatest"   % "2.2.5"  % "test"
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

// False positives:
// - Wart.Throw https://github.com/puffnfresh/wartremover/issues/182
// - Wart.IsInstanceOf & Wart.AsInstanceOf https://github.com/puffnfresh/wartremover/issues/152
// - Wart.ExplicitImplicitTypes https://github.com/puffnfresh/wartremover/issues/188
wartremoverErrors in (Compile, compile) ++=
  Warts.allBut(Wart.Throw, Wart.IsInstanceOf, Wart.AsInstanceOf, Wart.ExplicitImplicitTypes)
