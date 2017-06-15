package com.nthportal.versions
package semver

import com.nthportal.versions.extensions.Maven._
import com.nthportal.versions.semver.BuildMetadata._

@deprecated("testing deprecated methods", since = "1.3.0")
class DeprecatedSemVerFullTest extends SimpleSpec {
  behavior of "SemVerFull"

  it should "define equality correctly" in {
    val v0 = parseSemVerWithBuildMetadata("1.0.0-SNAPSHOT+build.12654")
    v0.buildMetadata.isDefined should be (true)
    v0.buildMetadata.get should be ("build.12654")

    val v1 = parseSemVerWithBuildMetadata("1.0.0-SNAPSHOT+12654")
    v1.buildMetadata.isDefined should be (true)
    v1.buildMetadata.get should be ("12654")

    v0 should not equal v1

    val v2 = parseSemVerWithBuildMetadata("1.0.0-SNAPSHOT")
    v2.buildMetadata.isDefined should be (false)

    v2 should not equal v0
    v2 should not equal v1
  }

  it should "compare correctly" in {
    val v0 = SemVerFull(v3.Version(1)(0)(0) -- Snapshot, Some("build.12654"))
    val v1 = SemVerFull(v3.Version(1)(0)(0) -- Snapshot, Some(12654))
    val v2 = SemVerFull(v3.Version(1)(0)(0) -- Snapshot, None)

    (v0 <= v1 && v0 >= v1) should be (true)
    (v0 <= v2 && v0 >= v2) should be (true)
    (v2 <= v1 && v2 >= v1) should be (true)
  }

  it should "produce the correct string representation" in {
    SemVerFull(v3.Version(1)(0)(0) -- Snapshot, Some("build.12654")).toString should be ("1.0.0-SNAPSHOT+build.12654")
    SemVerFull(v3.Version(1)(0)(0) -- Snapshot, None).toString should be ("1.0.0-SNAPSHOT")
  }
}
