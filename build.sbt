name := "play-slick-oauth2"

version := "0.0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  evolutions,
  "com.h2database" % "h2" % "1.4.191",
  "com.nulab-inc" %% "play2-oauth2-provider" % "0.17.0",
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0" % Test)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
