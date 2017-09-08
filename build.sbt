organization := "com.nthportal"
name := "versions"
description := "A Scala library for representing versions as objects."

val rawVersion = "3.0.0"
isSnapshot := true
version := rawVersion + { if (isSnapshot.value) "-SNAPSHOT" else "" }

scalaVersion := "2.12.3"
crossScalaVersions := Seq(
  "2.12.0",
  "2.12.1",
  "2.12.2",
  "2.12.3"
)

libraryDependencies ++= Seq(
  "com.nthportal" %% "extra-predef" % "1.+",
  "org.scalatest" %% "scalatest" % "3.0.+" % Test
)

scalacOptions ++= {
  if (isSnapshot.value) Seq()
  else scalaVersion.value split '.' map { _.toInt } match {
    case Array(2, 12, patch) if patch <= 2 => Seq("-opt:l:project")
    case Array(2, 12, patch) if patch > 2 => Seq("-opt:l:inline")
    case _ => Seq()
  }
}

publishTo := {
  if (isSnapshot.value) Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")
  else None
}

publishMavenStyle := true
licenses := Seq("The Apache License, Version 2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt"))
homepage := Some(url("https://github.com/NthPortal/versions"))

pomExtra :=
  <scm>
    <url>https://github.com/NthPortal/versions</url>
    <connection>scm:git:git@github.com:NthPortal/versions.git</connection>
    <developerConnection>scm:git:git@github.com:NthPortal/versions.git</developerConnection>
  </scm>
    <developers>
      <developer>
        <id>NthPortal</id>
        <name>NthPortal</name>
        <url>https://github.com/NthPortal</url>
      </developer>
    </developers>
