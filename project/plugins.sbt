addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.1.0-M9")

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.3.7")

addSbtPlugin("com.dwijnand" % "sbt-dynver" % "3.1.0")

addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.4")

addSbtPlugin("io.spray" % "sbt-boilerplate" % "0.6.1")

addSbtPlugin("com.dwijnand" % "sbt-travisci" % "1.1.3")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")

addSbtPlugin("com.codacy" % "sbt-codacy-coverage" % "2.112")

resolvers += Resolver.bintrayIvyRepo("zamblauskas", "sbt-plugins")
addSbtPlugin("zamblauskas" % "sbt-examplestest" % "0.1.2")
