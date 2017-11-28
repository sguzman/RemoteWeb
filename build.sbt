/** Name of project */
name := "RemoteWeb"

/** Organization */
organization := "com.github.sguzman"

/** Project Version */
version := "1.0"

/** Scala version */
scalaVersion := "2.12.4"

/** Scalac parameters */
scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

/** Javac parameters */
javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

/** Resolver */
resolvers ++= Seq(
  DefaultMavenRepository,
  Resolver.sonatypeRepo("public"),
  Resolver.sonatypeRepo("snapshots")
  Resolver.typesafeRepo("releases"),
  Resolver.sbtPluginRepo("releases"),
  Resolver.jcenterRepo,
)

/** Source Dependencies */
libraryDependencies ++= Seq(
  "org.scalaj" % "scalaj-http_2.12" % "2.3.0",
  "org.feijoas" % "mango_2.12" % "0.14",
  "org.apache.commons" % "commons-lang3" % "3.7"
)

/** Make sure to fork on run */
fork in run := true


herokuFatJar in Compile := Some((assemblyOutputPath in assembly).value)