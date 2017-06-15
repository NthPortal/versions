package com.nthportal.versions
package semver
package compat

import com.nthportal.versions.extensions.Maven._
import com.nthportal.versions.v3.compat.VersionFactory

@deprecated("testing deprecated methods", since = "1.3.0")
class DeprecatedSemVerFactoryTest extends SimpleSpec {

  private val versionFactory = VersionFactory(extensionDef, extensionParser)

  behavior of "SemVer factories"

  they should "parse regular versions correctly" in {
    val factory = SemVerFactory(versionFactory)

    val v = factory.parseVersion("1.0.0-SNAPSHOT+build.12654")

    v should equal (v3.Version(1)(0)(0) -- Snapshot)
    v should equal (factory.parseVersion("1.0.0-SNAPSHOT+12654"))
    v should equal (factory.parseVersion("1.0.0-SNAPSHOT"))

    v should equal (parseSemVerWithBuildMetadata("1.0.0-SNAPSHOT+build.12654").extendedVersion)

    a [VersionFormatException] should be thrownBy {factory.parseVersion("1.0.0-SNAPSHOT+")}
    a [VersionFormatException] should be thrownBy {factory.parseVersion("1.0.0-SNAPSHOT+build+12654")}
  }

  they should "parse full versions correctly" in {
    val factory = SemVerFullFactory(versionFactory, BuildMetadata.stringMetadataParser)

    val v = factory.parseVersion("1.0.0-SNAPSHOT+build.12654")
    v shouldEqual SemVerFull(v3.Version(1)(0)(0) -- Snapshot, Some("build.12654"))

    a [VersionFormatException] should be thrownBy {factory.parseVersion("1.0.0-SNAPSHOT+")}
    a [VersionFormatException] should be thrownBy {factory.parseVersion("1.0.0-SNAPSHOT+build+12654")}
  }
}
