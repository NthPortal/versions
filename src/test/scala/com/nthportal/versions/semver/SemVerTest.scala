package com.nthportal.versions
package semver

import com.nthportal.versions.extensions.Maven._
import com.nthportal.versions.semver.StringMetadata._

class SemVerTest extends SimpleSpec {
  behavior of "SemVer package object"

  it should "parse versions correctly" in {
    val v = parseSemVerVersion("1.0.0-SNAPSHOT+build.12654")

    v should equal (v3.Version(1)(0)(0) -- Snapshot)
    v should equal (parseSemVerVersion("1.0.0-SNAPSHOT+12654"))
    v should equal (parseSemVerVersion("1.0.0-SNAPSHOT"))

    v should equal (parseSemVerWithBuildMetadata("1.0.0-SNAPSHOT+build.12654").extendedVersion)

    a [VersionFormatException] should be thrownBy {parseSemVerVersion("1.0.0-SNAPSHOT+")}
    a [VersionFormatException] should be thrownBy {parseSemVerVersion("1.0.0-SNAPSHOT+build+12654")}
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