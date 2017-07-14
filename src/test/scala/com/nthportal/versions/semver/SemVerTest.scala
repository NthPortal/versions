package com.nthportal.versions
package semver

import com.nthportal.versions.extensions.Maven._
import com.nthportal.versions.semver.BuildMetadata._

class SemVerTest extends SimpleSpec {
  behavior of "SemVer package object"

  it should "parse versions correctly" in {
    val v = parseSemVer("1.0.0-SNAPSHOT+build.12654").extendedVersion

    v should equal (v3.Version(1)(0)(0) -- Snapshot)
    v shouldEqual parseSemVerWithoutMetadata("1.0.0-SNAPSHOT")
    v shouldEqual parseSemVer("1.0.0-SNAPSHOT+12654").extendedVersion

    a [VersionFormatException] should be thrownBy {parseSemVerWithoutMetadata("1.0.0-SNAPSHOT+12654")}
    a [VersionFormatException] should be thrownBy {parseSemVer("1.0.0-SNAPSHOT+")}
    a [VersionFormatException] should be thrownBy {parseSemVer("1.0.0-SNAPSHOT+build+12654")}
  }

  it should "only allow valid SemVer extensions and metadata" in {
    implicit val extensionParser: ExtensionParser[String] = identity(_)
    implicit val extensionDef: ExtensionDef[String] = ExtensionDef(None, _ compare _)

    a [VersionFormatException] should be thrownBy {parseSemVer("1.0.0-SNAP_SHOT")}
    a [VersionFormatException] should be thrownBy {parseSemVer("1.0.0-SNAP..SHOT")}
    a [VersionFormatException] should be thrownBy {parseSemVer("1.0.0-SNAPSHOT?")}
    a [VersionFormatException] should be thrownBy {parseSemVer("1.0.0-.SNAPSHOT")}
    a [VersionFormatException] should be thrownBy {parseSemVer("1.0.0-SNAP.SHOT.")}

    noException should be thrownBy {parseSemVer("1.0.0-SNAP.SHOT")}
    noException should be thrownBy {parseSemVer("1.0.0-SNAP-SHOT")}
    noException should be thrownBy {parseSemVer("1.0.0--SNAPSHOT")}
    noException should be thrownBy {parseSemVer("1.0.0-SNAP--SHOT")}
    noException should be thrownBy {parseSemVer("1.0.0-SNAPSHOT-")}

    a [VersionFormatException] should be thrownBy {parseSemVer("1.0.0-SNAPSHOT+build..12654")}
    a [VersionFormatException] should be thrownBy {parseSemVer("1.0.0-SNAPSHOT+.build.12654")}
    a [VersionFormatException] should be thrownBy {parseSemVer("1.0.0-SNAPSHOT+build.12654.")}
    a [VersionFormatException] should be thrownBy {parseSemVer("1.0.0-SNAPSHOT+build_12654")}

    noException should be thrownBy {parseSemVer("1.0.0-SNAPSHOT+build.12654")}
    noException should be thrownBy {parseSemVer("1.0.0-SNAPSHOT+build-12654")}
    noException should be thrownBy {parseSemVer("1.0.0-SNAPSHOT+-build.12654")}
    noException should be thrownBy {parseSemVer("1.0.0-SNAPSHOT+build--12654")}
    noException should be thrownBy {parseSemVer("1.0.0-SNAPSHOT+build.12654-")}
  }

  it should "parse versions correctly as `Option`s" in {
    import BuildMetadata.stringMetadataParser

    parseSemVerAsOption("1.0.0-SNAPSHOT+build.12654").value shouldEqual (v3.Version(1)(0)(0) -- Snapshot).withBuildMetadata("build.12654")
    parseSemVerAsOption("1.0.0").value shouldEqual (v3.Version(1)(0)(0) -- Release).withNoMetadata[String]

    parseSemVerAsOption("1.0.0-SNAPSHOT+") shouldBe empty
    parseSemVerAsOption("1.0.0-SNAPSHOT+build+12654") shouldBe empty
  }

  it should "pattern match versions correctly" in {
    // SemVer
    inside("1.0.0-SNAPSHOT+build.12654") { case SemVer(v3.Version(1, 0, 0), Snapshot, Some("build.12654")) => }
    inside("1.0.0") { case SemVer(v3.Version(1, 0, 0), Release, None) => }

    SemVer unapply "1.0.0-SNAPSHOT+" shouldBe empty
    SemVer unapply "1.0.0-SNAPSHOT+build+12654" shouldBe empty

    // +
    inside(v3.V(1, 0, 0) -- Snapshot + "build.12654") { case v3.V(1, 0, 0) -- Snapshot + "build.12654" => }
    semver.+.unapply((v3.V(1, 0, 0) -- Release).withNoMetadata[String]) shouldBe empty

    // +?
    inside(v3.V(1, 0, 0) -- Snapshot + "build.12654") { case v3.V(1, 0, 0) -- Snapshot +? Some("build.12654") => }
    inside(v3.V(1, 0, 0) -- Release +? None) { case v3.V(1, 0, 0) -- Release +? None => }
  }

  it should "bump versions correctly" in {
    val v1 = v3.Version(1)(1)(1) -- Snapshot

    v1.bumpMajor should be (v3.Version(2)(0)(0) -- Snapshot)
    v1.bumpMinor should be (v3.Version(1)(2)(0) -- Snapshot)
    v1.bumpPatch should be (v3.Version(1)(1)(2) -- Snapshot)

    val v2 = v3.Version(1)(2)(0) -- Release
    v2.version.bumpMinor -- Snapshot should be (v3.Version(1)(3)(0) -- Snapshot)
  }
}
