package com.nthportal.versions
package v4

import com.nthportal.versions.extensions.Maven
import com.nthportal.versions.extensions.Maven._

class ExtendedVersionTest extends SimpleSpec {

  behavior of "ExtendedVersion (4)"

  it should "have consistent constructors" in {
    Version(1)(2)(5)(4) -- Snapshot shouldEqual ExtendedVersion(Version(1)(2)(5)(4), Snapshot, extensionDef)
  }

  it should "compare correctly" in {
    val v1 = Version(1)(2)(5)(4) -- Snapshot

    v1 should be > Version(1)(2)(5)(3) -- Release
    v1 should be < Version(1)(2)(5)(4) -- Release

    val v2 = Version(2)(0)(0)(0) -- Release

    v2 should be < Version(2)(0)(0)(1) -- Snapshot
    v2 should be > Version(2)(0)(0)(0) -- Snapshot

    an [IllegalArgumentException] should be thrownBy {
      v1.compare(Version(1)(2)(5)(4).dash(Snapshot)(ExtensionDef.fromOrdered[Maven]))
    }
  }

  it should "produce the correct string representation" in {
    (Version(1)(2)(5)(4) -- Snapshot).toString shouldBe "1.2.5.4-SNAPSHOT"
    (Version(1)(2)(5)(4) -- Release).toString shouldBe "1.2.5.4"
  }

  it should "parse versions correctly" in {
    ExtendedVersion parseVersion "1.2.5.4" should equal (Version(1)(2)(5)(4) -- Release)
    ExtendedVersion parseVersion "0.0.0.0-SNAPSHOT" should equal (Version(0)(0)(0)(0) -- Snapshot)

    a [VersionFormatException] should be thrownBy {ExtendedVersion parseVersion "1.0.0.0-INVALID"}
    a [VersionFormatException] should be thrownBy {ExtendedVersion parseVersion "1.0.0.0-RELEASE"}
    a [VersionFormatException] should be thrownBy {ExtendedVersion parseVersion "1.0.0.0-snapshot"}
    a [VersionFormatException] should be thrownBy {ExtendedVersion parseVersion "1.0.0.0-SNAPSHOT-4"}
    a [VersionFormatException] should be thrownBy {ExtendedVersion parseVersion "really not a version"}

    // Missing default extension
    a [VersionFormatException] should be thrownBy {
      ExtendedVersion.parseVersion("1.0.0.0")(ExtensionDef.fromOrdered[Maven], extensionParser)
    }
  }

  it should "parse versions as options correctly" in {
    ExtendedVersion.parseAsOption("1.2.5.4").value should equal (Version(1)(2)(5)(4) -- Release)
    ExtendedVersion.parseAsOption("0.0.0.0-SNAPSHOT").value should equal (Version(0)(0)(0)(0) -- Snapshot)

    ExtendedVersion parseAsOption "1.0.0.0-INVALID" shouldBe empty
    ExtendedVersion parseAsOption "1.0.0.0-RELEASE" shouldBe empty
    ExtendedVersion parseAsOption "1.0.0.0-snapshot" shouldBe empty
    ExtendedVersion parseAsOption "1.0.0.0-SNAPSHOT-4" shouldBe empty
    ExtendedVersion parseAsOption "really not a version" shouldBe empty

    // Missing default extension
    ExtendedVersion.parseAsOption("1.0.0.0")(ExtensionDef.fromOrdered[Maven], extensionParser) shouldBe empty
  }

  it should "pattern match versions correctly" in {
    inside("1.2.5.4") { case ExtendedVersion(Version(1, 2, 5, 4), Release) => }
    inside("0.0.0.0-SNAPSHOT") { case ExtendedVersion(Version(0, 0, 0, 0), Snapshot) => }

    ExtendedVersion unapply "1.0.0.0-INVALID" shouldBe empty
    ExtendedVersion unapply "1.0.0.0-RELEASE" shouldBe empty
    ExtendedVersion unapply "1.0.0.0-snapshot" shouldBe empty
    ExtendedVersion unapply "1.0.0.0-SNAPSHOT-4" shouldBe empty
    ExtendedVersion unapply "really not a version" shouldBe empty

    // Missing default extension
    ExtendedVersion.unapply("1.0.0.0")(ExtensionDef.fromOrdered[Maven], extensionParser) shouldBe empty
  }
}
