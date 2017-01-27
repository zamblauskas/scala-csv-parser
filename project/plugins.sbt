addSbtPlugin("org.wartremover" % "sbt-wartremover" % "1.1.1")

addSbtPlugin("com.dwijnand" % "sbt-dynver" % "1.1.1")

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")

addSbtPlugin("io.spray" % "sbt-boilerplate" % "0.6.0")

addSbtPlugin("com.dwijnand" % "sbt-travisci" % "1.0.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")

addSbtPlugin("com.codacy" % "sbt-codacy-coverage" % "1.3.8")

resolvers += Resolver.bintrayIvyRepo("zamblauskas", "sbt-plugins")
addSbtPlugin("zamblauskas" % "sbt-examplestest" % "0.1.1")

