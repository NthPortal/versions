package com.nthportal.versions
package v3

import com.nthportal.versions.extensions.Maven
import com.nthportal.versions.extensions.Maven._

class ExtendedVersionTest extends SimpleSpec {

  behavior of "ExtendedVersion (3)"

  it should "have consistent constructors" in {
    Version(1)(2)(5) -- Snapshot should equal(ExtendedVersion(Version(1)(2)(5), Snapshot, extensionDef))
  }

  it should "compare correctly" in {
    val v1 = Version(1)(2)(5) -- Snapshot

    v1 should be > Version(1)(2)(4) -- Release
    v1 should be < Version(1)(2)(5) -- Release

    val v2 = Version(2)(0)(0) -- Release

    v2 should be < Version(2)(0)(1) -- Snapshot
    v2 should be > Version(2)(0)(0) -- Snapshot
  }

  it should "produce the correct string representation" in {
    (Version(1)(2)(5) -- Snapshot).toString should be("1.2.5-SNAPSHOT")
    (Version(1)(2)(5) -- Release).toString should be("1.2.5")
  }

  it should "parse versions correctly" in {
    ExtendedVersion parseVersion "1.2.5" should equal(Version ⋮ 1⋅2⋅5 -- Release)
    ExtendedVersion parseVersion "0.0.0-SNAPSHOT" should equal(Version ⋮ 0⋅0⋅0 -- Snapshot)

    a [VersionFormatException] should be thrownBy {ExtendedVersion parseVersion "1.0.0-INVALID"}
    a [VersionFormatException] should be thrownBy {ExtendedVersion parseVersion "1.0.0-RELEASE"}
    a [VersionFormatException] should be thrownBy {ExtendedVersion parseVersion "1.0.0-snapshot"}
    a [VersionFormatException] should be thrownBy {ExtendedVersion parseVersion "1.0.0-SNAPSHOT-4"}
    a [VersionFormatException] should be thrownBy {ExtendedVersion parseVersion "really not a version"}

    // Missing default extension
    a [VersionFormatException] should be thrownBy {
      ExtendedVersion.parseVersion("1.0.0")(ExtensionDef.fromOrdered[Maven], parser)
    }
  }
}
