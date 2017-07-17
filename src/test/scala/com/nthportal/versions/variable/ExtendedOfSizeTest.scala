package com.nthportal.versions
package variable

import com.nthportal.versions.extensions.Maven
import com.nthportal.versions.extensions.Maven._

class ExtendedOfSizeTest extends SimpleSpec {
  behavior of "ExtendedVersions.ofSize"

  it should "only allow valid sizes" in {
    noException should be thrownBy { ExtendedVersions.ofSize(1 to 10) }

    an [IllegalArgumentException] should be thrownBy { ExtendedVersions.ofSize(2 until 2) }
    an [IllegalArgumentException] should be thrownBy { ExtendedVersions.ofSize(0 to 5) }
  }

  it should "create versions correctly" in {
    val EV = ExtendedVersions.ofSize(1 to 2)

    val ed = implicitly[ExtensionDef[Maven]]

    noException should be thrownBy { EV(Version(1, 3), Snapshot, ed) }
    noException should be thrownBy { EV(Version(1, 3), Snapshot, ed) }
    noException should be thrownBy { EV(Version(1, 3), Snapshot, ed) }

    an [IllegalArgumentException] should be thrownBy { EV(Version(1, 2, 5), Snapshot, ed) }
    an [IllegalArgumentException] should be thrownBy {
      ExtendedVersion(Versions.ofSize(1 to 20)(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16), Snapshot, ed)
    }
  }

  it should "parse versions correctly" in {
    val V = Versions.ofSize(1 to 2)
    val EV = V.extended

    EV parseVersion "1.3-SNAPSHOT" shouldEqual V(1, 3) -- Snapshot
    EV parseVersion "0.0" shouldEqual V(0, 0) -- Release
    EV parseVersion "12" shouldEqual V(12) -- Release

    a [VersionFormatException] should be thrownBy { EV parseVersion "1.2.5-SNAPSHOT" }
    a [VersionFormatException] should be thrownBy { EV parseVersion "1.0.0" }
  }

  it should "parse versions as options correctly" in {
    val V = Versions.ofSize(1 to 2)
    val EV = V.extended

    EV.parseAsOption("1.3-SNAPSHOT").value shouldEqual V(1, 3) -- Snapshot
    EV.parseAsOption("0.0").value shouldEqual V(0, 0) -- Release
    EV.parseAsOption("12").value shouldEqual V(12) -- Release

    EV parseAsOption "1.2.5-SNAPSHOT" shouldBe empty
    EV parseAsOption "1.0.0" shouldBe empty
  }

  it should "pattern match version strings correctly" in {
    val V = Versions.ofSize(1 to 2)
    val EV = V.extended

    inside("1.3-SNAPSHOT") { case EV(V(1, 3), Snapshot) => }
    inside("0.0") { case EV(V(0, 0), Release) => }
    inside("12") { case EV(V(12), Release) => }

    EV unapply "1.2.5-SNAPSHOT" shouldBe empty
    EV unapply "1.0.0" shouldBe empty
  }
}
