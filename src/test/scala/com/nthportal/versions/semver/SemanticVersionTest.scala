package com.nthportal.versions
package semver

import com.nthportal.convert.Convert
import com.nthportal.versions.extensions.Maven._
import com.nthportal.versions.semver.BuildMetadata._

class SemanticVersionTest extends SimpleSpec {
  behavior of "SemanticVersion"

  it should "return the correct version and extension" in {
    val v = v3.V(1, 0, 0) -- Snapshot + 12654

    v.version should be theSameInstanceAs v.extendedVersion.version
    v.extension should be theSameInstanceAs v.extendedVersion.extension
  }

  it should "define equality correctly" in {
    import Convert.Valid.Implicit.ref

    val v0 = parseSemVer("1.0.0-SNAPSHOT+build.12654")
    v0.buildMetadata.value shouldBe "build.12654"

    val v1 = parseSemVer("1.0.0-SNAPSHOT+12654")
    v1.buildMetadata.value shouldBe "12654"

    v0 should not equal v1

    val v2 = parseSemVer("1.0.0-SNAPSHOT")
    v2.buildMetadata shouldBe empty

    v2 should not equal v0
    v2 should not equal v1
  }

  it should "compare correctly" in {
    val v0 = v3.Version(1)(0)(0) -- Snapshot + "build.12654"
    val v1 = v3.Version(1)(0)(0) -- Snapshot + 12654
    val v2 = (v3.Version(1)(0)(0) -- Snapshot).withNoMetadata

    (v0 <= v1 && v0 >= v1) shouldBe true
    (v0 <= v2 && v0 >= v2) shouldBe true
    (v2 <= v1 && v2 >= v1) shouldBe true
  }

  it should "produce the correct string representation" in {
    (v3.Version(1)(0)(0) -- Snapshot + "build.12654").toString shouldBe "1.0.0-SNAPSHOT+build.12654"
    (v3.Version(1)(0)(0) -- Snapshot).withNoMetadata.toString shouldBe "1.0.0-SNAPSHOT"
  }
}
