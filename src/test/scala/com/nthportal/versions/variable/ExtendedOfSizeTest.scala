package com.nthportal.versions
package variable

import com.nthportal.convert.Convert
import com.nthportal.versions.extensions.Maven
import com.nthportal.versions.extensions.Maven._

class ExtendedOfSizeTest extends SimpleSpec {
  behavior of "ExtendedVersion.ofSize"

  it should "only allow valid sizes" in {
    noException should be thrownBy { ExtendedVersion.ofSize(1 to 10) }

    an [IllegalArgumentException] should be thrownBy { ExtendedVersion.ofSize(2 until 2) }
    an [IllegalArgumentException] should be thrownBy { ExtendedVersion.ofSize(0 to 5) }
  }

  it should "create versions correctly" in {
    val EV = ExtendedVersion.ofSize(1 to 2)

    val ed = implicitly[ExtensionDef[Maven]]

    noException should be thrownBy { EV(Version(1, 3), Snapshot, ed) }
    noException should be thrownBy { EV(Version(1, 3), Snapshot, ed) }
    noException should be thrownBy { EV(Version(1, 3), Snapshot, ed) }

    an [IllegalArgumentException] should be thrownBy { EV(Version(1, 2, 5), Snapshot, ed) }
  }

  it should "parse versions correctly" in {
    import Convert.Throwing.Implicit.ref
    val V = Version.ofSize(1 to 2)
    val EV = V.extended

    EV parseVersion "1.3-SNAPSHOT" shouldEqual V(1, 3) -- Snapshot
    EV parseVersion "0.0" shouldEqual V(0, 0) -- Release
    EV parseVersion "12" shouldEqual V(12) -- Release

    a [VersionFormatException] should be thrownBy { EV parseVersion "1.2.5-SNAPSHOT" }
    a [VersionFormatException] should be thrownBy { EV parseVersion "1.0.0" }
  }

  it should "parse versions as options correctly" in {
    import Convert.AsOption.Implicit.ref
    val V = Version.ofSize(1 to 2)
    val EV = V.extended

    EV.parseVersion("1.3-SNAPSHOT").value shouldEqual V(1, 3) -- Snapshot
    EV.parseVersion("0.0").value shouldEqual V(0, 0) -- Release
    EV.parseVersion("12").value shouldEqual V(12) -- Release

    EV parseVersion "1.2.5-SNAPSHOT" shouldBe empty
    EV parseVersion "1.0.0" shouldBe empty
  }

  it should "pattern match version strings correctly" in {
    val V = Version.ofSize(1 to 2)
    val EV = V.extended

    inside("1.3-SNAPSHOT") { case EV(V(1, 3), Snapshot) => }
    inside("0.0") { case EV(V(0, 0), Release) => }
    inside("12") { case EV(V(12), Release) => }

    EV unapply "1.2.5-SNAPSHOT" shouldBe empty
    EV unapply "1.0.0" shouldBe empty
  }

  it should "convert to other types correctly" in {
    import Convert.Throwing.Implicit.ref
    val ev1 = Version(1, 2, 5) -- Snapshot

    ev1 to ExtendedVersion.ofSize(2 to 3) shouldEqual ev1
    an [IllegalArgumentException] should be thrownBy { ev1 to ExtendedVersion.ofSize(1 to 2) }

    val ev2 = Version.ofSize(1 to 2)(1) -- Snapshot

    ev2 to ExtendedVersion shouldEqual ev2
    an [IllegalArgumentException] should be thrownBy { ev2 to ExtendedVersion.ofSize(2 to 3) }
  }

  it should "convert as an option to other types correctly" in {
    import Convert.AsOption.Implicit.ref
    val v1 = Version(1, 2, 5) -- Snapshot

    v1.to(ExtendedVersion.ofSize(2 to 3)).value shouldEqual v1
    v1 to ExtendedVersion.ofSize(1 to 2) shouldBe empty

    val v2 = Version.ofSize(1 to 2)(1) -- Snapshot

    v2.to(ExtendedVersion).value shouldEqual v2
    v2 to ExtendedVersion.ofSize(2 to 3) shouldBe empty
  }
}
