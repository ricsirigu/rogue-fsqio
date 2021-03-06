// Copyright 2012 Foursquare Labs Inc. All Rights Reserved.
import sbt._
import Keys.{scalaVersion, _}

object RogueSettings {

  val sonatypeReleases = "releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
  val sonatypeSnapshots = "snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

  lazy val defaultSettings: Seq[Setting[_]] = Seq(
    version := "3.1.17",
    organization := "com.github.ricsirigu",
	homepage := Some(url("https://github.com/ricsirigu/lift-omniauth")),
	scmInfo := Some(ScmInfo(
	  url("https://github.com/ricsirigu/rogue-fsqio"), "git@github.com:ricsirigu/rogue-fsqio.git")
	),
	developers += Developer("ricsirigu",
	  "ricsirigu",
	  "me@riccardosirigu.com",
	  url("https://github.com/ricsirigu")),
	licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
    crossScalaVersions := Seq("2.11.11","2.12.3"),
    scalaVersion := "2.11.11",
    isSnapshot := true,
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    publishTo := version { v =>
      if (v.endsWith("-SNAPSHOT"))
        Some(sonatypeSnapshots)
      else
        Some(sonatypeReleases)
    }.value,
    resolvers ++= Seq(sonatypeReleases, sonatypeSnapshots),
    scalacOptions ++= Seq("-deprecation", "-unchecked"), //, "-Xlog-implicit-conversions"),
    scalacOptions ++= Seq("-feature", "-language:_"),
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials") ,
    testOptions in Test ++= Seq(Tests.Setup(() => MongoEmbedded.start), Tests.Cleanup(()=> MongoEmbedded.stop))
	)
}

object RogueDependencies {
  val liftVersion = "3.1.1"
  val specsVer = "3.8.6"
  val liftDeps = Seq(
    "net.liftweb"              %% "lift-mongodb"    % liftVersion  % "compile" intransitive(),
    "net.liftweb"              %% "lift-common"     % liftVersion  % "compile",
    "net.liftweb"              %% "lift-json"       % liftVersion  % "compile",
    "net.liftweb"              %% "lift-util"       % liftVersion  % "compile"
  )
  
  val liftRecordDeps = Seq(
  "net.liftweb"              %% "lift-record"         % liftVersion  % "compile" intransitive(),
  "net.liftweb"              %% "lift-mongodb-record" % liftVersion  % "compile" intransitive(),
  "net.liftweb"              %% "lift-webkit"         % liftVersion  % "compile" intransitive()
  )
  
  
  val joda = Seq(
    "joda-time"                % "joda-time"           % "2.9.9"        % "compile",
    "org.joda"                 % "joda-convert"        % "1.8.1"        % "compile"
  )
  val mongoDeps = Seq(
    "org.mongodb"              % "mongodb-driver"      % "3.4.2"     % "compile",
    "org.mongodb"              % "mongodb-driver-async"% "3.4.2"     % "compile"
  )

  val testDeps = Seq(
    "junit"                    % "junit"               % "4.5"        % "test",
    "org.specs2"              %% "specs2-core"              % specsVer % "test",
    "org.specs2"              %% "specs2-matcher"              % specsVer % "test",
    "org.specs2"              %% "specs2-junit"              % specsVer % "test",
    "org.scalatest" %% "scalatest" % "3.0.3" % "test",
    "com.novocode" % "junit-interface" % "0.11" % "test",
    "org.slf4j" % "slf4j-simple" % "1.7.21" % "test"
  )

  val shapeless = "com.chuusai" %% "shapeless" % "2.3.2"

  val coreDeps = mongoDeps ++ joda
  
  val rogueLiftDeps = mongoDeps ++ joda ++ liftDeps ++ liftRecordDeps

  val ccDeps = mongoDeps ++ Seq(shapeless)  ++ testDeps
}
