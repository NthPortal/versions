package com.nthportal.versions
package semver

import com.nthportal.versions.extensions.Maven._
import com.nthportal.versions.semver.BuildMetadata._

class SemanticVersionTest extends SimpleSpec {
  behavior of "SemanticVersion"

  it should "define equality correctly" in {
    val v0 = parseSemVer("1.0.0-SNAPSHOT+build.12654")
    v0.buildMetadata.isDefined should be (true)
    v0.buildMetadata.get should be ("build.12654")

    val v1 = parseSemVer("1.0.0-SNAPSHOT+12654")
    v1.buildMetadata.isDefined should be (true)
    v1.buildMetadata.get should be ("12654")

    v0 should not equal v1

    val v2 = parseSemVer("1.0.0-SNAPSHOT")
    v2.buildMetadata.isDefined should be (false)

    v2 should not equal v0
    v2 should not equal v1
  }

  it should "compare correctly" in {
    val v0 = v3.Version(1)(0)(0) -- Snapshot + "build.12654"
    val v1 = v3.Version(1)(0)(0) -- Snapshot + 12654
    val v2 = (v3.Version(1)(0)(0) -- Snapshot).withNoMetadata

    (v0 <= v1 && v0 >= v1) should be (true)
    (v0 <= v2 && v0 >= v2) should be (true)
    (v2 <= v1 && v2 >= v1) should be (true)
  }

  it should "produce the correct string representation" in {
    (v3.Version(1)(0)(0) -- Snapshot + "build.12654").toString should be ("1.0.0-SNAPSHOT+build.12654")
    (v3.Version(1)(0)(0) -- Snapshot).withNoMetadata.toString should be ("1.0.0-SNAPSHOT")
  }
}
