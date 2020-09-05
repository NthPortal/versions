ThisBuild / scalaVersion := "2.13.3"
ThisBuild / autoAPIMappings := true
ThisBuild / crossScalaVersions := Seq(
  "2.12.10",
  "2.13.3"
)

// publishing info
inThisBuild(
  Seq(
    organization := "com.nthportal",
    homepage := Some(url("https://github.com/NthPortal/versions")),
    licenses := Seq("The Apache License, Version 2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt")),
    developers := List(
      Developer(
        "NthPortal",
        "April | Princess",
        "dev@princess.lgbt",
        url("https://nthportal.com")
      )
    ),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/NthPortal/versions"),
        "scm:git:git@github.com:NthPortal/versions.git",
        "scm:git:git@github.com:NthPortal/versions.git"
      )
    )
  )
)

lazy val extraPredef = project
  .in(file("."))
  .settings(
    name := "versions",
    description := "A Scala library for representing versions as objects.",
    mimaPreviousArtifacts := {
      val previousVersions = CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 12)) => Set("2.0.0", "2.0.1")
        case _             => Set()
      }
      previousVersions.map(organization.value %% name.value % _)
    },
    libraryDependencies ++= Seq(
      "com.nthportal" %% "extra-predef" % "2.1.0",
      "org.scalatest" %% "scalatest"    % "3.2.2" % Test
    ),
    scalacOptions ++= {
      if (isSnapshot.value) Nil
      else Seq("-opt:l:inline", "-opt-inline-from:com.nthportal.versions.**")
    }
  )
