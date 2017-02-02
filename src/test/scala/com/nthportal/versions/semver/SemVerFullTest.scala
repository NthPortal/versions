package com.nthportal.versions
package semver

import com.nthportal.versions.extensions.Maven._
import com.nthportal.versions.semver.StringMetadata._

class SemVerFullTest extends SimpleSpec {
  behavior of "SemVerFull"

  it should "define equality correctly" in {
    val v0 = parseSemVerWithBuildMetadata("1.0.0-SNAPSHOT+build.12654")
    v0.buildMetadata.isDefined should be (true)
    v0.buildMetadata.get.value should be ("build.12654")

    val v1 = parseSemVerWithBuildMetadata("1.0.0-SNAPSHOT+12654")
    v1.buildMetadata.isDefined should be (true)
    v1.buildMetadata.get.value should be ("12654")

    v0 should not equal v1

    val v2 = parseSemVerWithBuildMetadata("1.0.0-SNAPSHOT")
    v2.buildMetadata.isDefined should be (false)

    v2 should not equal v0
    v2 should not equal v1
  }

  it should "compare correctly" in {
    val v0 = v3.Version(1)(0)(0) -- Snapshot + StringMetadata("build.12654")
    val v1 = v3.Version(1)(0)(0) -- Snapshot + StringMetadata("12654")
    val v2 = (v3.Version(1)(0)(0) -- Snapshot).withNoMetadata[StringMetadata]

    v0 shouldNot be > v1
    v0 shouldNot be < v1
    v0 shouldNot be > v2
    v0 shouldNot be < v2
  }

  it should "produce the correct string representation" in {
    (v3.Version(1)(0)(0) -- Snapshot + StringMetadata("build.12654")).toString should be ("1.0.0-SNAPSHOT+build.12654")
    (v3.Version(1)(0)(0) -- Snapshot).withNoMetadata.toString should be ("1.0.0-SNAPSHOT")
  }
}
