package com.nthportal.versions
package v2

import com.nthportal.versions.extensions.Maven
import com.nthportal.versions.extensions.Maven._

class ExtendedVersionTest extends SimpleSpec {

  behavior of "ExtendedVersion (2)"

  it should "have consistent constructors" in {
    Version(1)(3) -- Snapshot shouldEqual ExtendedVersion(Version(1)(3), Snapshot, extensionDef)
  }

  it should "compare correctly" in {
    val v1 = Version(1)(3) -- Snapshot

    v1 should be > Version(1)(2) -- Release
    v1 should be < Version(1)(3) -- Release

    val v2 = Version(2)(0) -- Release

    v2 should be < Version(2)(1) -- Snapshot
    v2 should be > Version(2)(0) -- Snapshot

    an [IllegalArgumentException] should be thrownBy {
      v1.compare(Version(1)(3).dash(Snapshot)(ExtensionDef.fromOrdered[Maven]))
    }
  }

  it should "produce the correct string representation" in {
    (Version(1)(3) -- Snapshot).toString shouldBe "1.3-SNAPSHOT"
    (Version(1)(3) -- Release).toString shouldBe "1.3"
  }

  it should "parse versions correctly" in {
    ExtendedVersion parseVersion "1.3" should equal (Version(1)(3) -- Release)
    ExtendedVersion parseVersion "0.0-SNAPSHOT" should equal (Version(0)(0) -- Snapshot)

    a [VersionFormatException] should be thrownBy {ExtendedVersion parseVersion "1.0-INVALID"}
    a [VersionFormatException] should be thrownBy {ExtendedVersion parseVersion "1.0-RELEASE"}
    a [VersionFormatException] should be thrownBy {ExtendedVersion parseVersion "1.0-snapshot"}
    a [VersionFormatException] should be thrownBy {ExtendedVersion parseVersion "1.0-SNAPSHOT-4"}
    a [VersionFormatException] should be thrownBy {ExtendedVersion parseVersion "really not a version"}

    // Missing default extension
    a [VersionFormatException] should be thrownBy {
      ExtendedVersion.parseVersion("1.0")(ExtensionDef.fromOrdered[Maven], extensionParser)
    }
  }

  it should "parse versions as options correctly" in {
    ExtendedVersion.parseAsOption("1.3").value should equal (Version(1)(3) -- Release)
    ExtendedVersion.parseAsOption("0.0-SNAPSHOT").value should equal (Version(0)(0) -- Snapshot)

    ExtendedVersion parseAsOption "1.0-INVALID" shouldBe empty
    ExtendedVersion parseAsOption "1.0-RELEASE" shouldBe empty
    ExtendedVersion parseAsOption "1.0-snapshot" shouldBe empty
    ExtendedVersion parseAsOption "1.0-SNAPSHOT-4" shouldBe empty
    ExtendedVersion parseAsOption "really not a version" shouldBe empty

    // Missing default extension
    ExtendedVersion.parseAsOption("1.0")(ExtensionDef.fromOrdered[Maven], extensionParser) shouldBe empty
  }

  it should "pattern match versions correctly" in {
    inside("1.3") { case ExtendedVersion(Version(1, 3), Release) => }
    inside("0.0-SNAPSHOT") { case ExtendedVersion(Version(0, 0), Snapshot) => }

    ExtendedVersion unapply "1.0-INVALID" shouldBe empty
    ExtendedVersion unapply "1.0-RELEASE" shouldBe empty
    ExtendedVersion unapply "1.0-snapshot" shouldBe empty
    ExtendedVersion unapply "1.0-SNAPSHOT-4" shouldBe empty
    ExtendedVersion unapply "really not a version" shouldBe empty

    // Missing default extension
    ExtendedVersion.unapply("1.0")(ExtensionDef.fromOrdered[Maven], extensionParser) shouldBe empty
  }

  it should "convert to other types correctly" in {
    val ev = Version(1, 3) -- Snapshot

    ev.to(ExtendedVersion).value shouldEqual ev
    ev.to(variable.ExtendedVersion).value shouldEqual variable.Version(1, 3) -- Snapshot

    ev.to(v3.ExtendedVersion) shouldBe empty
    ev.to(v4.ExtendedVersion) shouldBe empty
  }
}
