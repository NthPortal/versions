organization := "com.nthportal"
name := "versions"
description := "A Scala library for representing versions as objects."

val rawVersion = "1.1.0"
isSnapshot := false
version := rawVersion + {if (isSnapshot.value) "-SNAPSHOT" else ""}

scalaVersion := "2.12.1"
crossScalaVersions := Seq(
  "2.11.8",
  "2.12.0",
  "2.12.1"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.+" % Test
)

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
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
