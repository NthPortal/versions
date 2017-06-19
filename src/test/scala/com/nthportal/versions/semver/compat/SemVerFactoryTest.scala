package com.nthportal.versions
package semver
package compat

import com.nthportal.versions.extensions.Maven._
import com.nthportal.versions.v3.compat.VersionFactory

class SemVerFactoryTest extends SimpleSpec {

  private val versionFactory = VersionFactory(extensionDef, extensionParser)

  behavior of "SemVer factories"

  they should "parse versions without metadata correctly" in {
    val factory = SemVerWithoutMetadataFactory(versionFactory)

    val v = factory.parseVersion("1.0.0-SNAPSHOT")

    v shouldEqual (v3.Version(1)(0)(0) -- Snapshot)
    v shouldEqual parseSemVer("1.0.0-SNAPSHOT+build.12654").extendedVersion

    a [VersionFormatException] should be thrownBy {factory.parseVersion("1.0.0-SNAPSHOT+12654")}
    a [VersionFormatException] should be thrownBy {factory.parseVersion("1.0.0-SNAPSHOT+")}
    a [VersionFormatException] should be thrownBy {factory.parseVersion("1.0.0-SNAPSHOT+build+12654")}
  }

  they should "parse versions correctly" in {
    val factory = SemanticVersionFactory(versionFactory, BuildMetadata.stringMetadataParser)

    val v = factory.parseVersion("1.0.0-SNAPSHOT+build.12654")
    v should equal (v3.Version(1)(0)(0) -- Snapshot + "build.12654")

    a [VersionFormatException] should be thrownBy {factory.parseVersion("1.0.0-SNAPSHOT+")}
    a [VersionFormatException] should be thrownBy {factory.parseVersion("1.0.0-SNAPSHOT+build+12654")}
  }
}
