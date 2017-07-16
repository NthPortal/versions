package com.nthportal.versions
package v4
package compat

import com.nthportal.versions.extensions.Maven
import com.nthportal.versions.extensions.Maven._

class VersionFactoryTest extends SimpleSpec {

  behavior of "VersionFactory (4)"

  it should "parse versions correctly" in {
    val f = VersionFactory(extensionDef, extensionParser)

    f parseVersion "1.2.5.4" should equal(Version(1)(2)(5)(4) -- Release)
    f parseVersion "0.0.0.0-SNAPSHOT" should equal(Version(0)(0)(0)(0) -- Snapshot)

    a [VersionFormatException] should be thrownBy {f parseVersion "1.0.0.0-INVALID"}
    a [VersionFormatException] should be thrownBy {f parseVersion "1.0.0.0-RELEASE"}
    a [VersionFormatException] should be thrownBy {f parseVersion "1.0.0.0-snapshot"}
    a [VersionFormatException] should be thrownBy {f parseVersion "1.0.0.0-SNAPSHOT-4"}
    a [VersionFormatException] should be thrownBy {f parseVersion "really not a version"}

    // Missing default extension
    a [VersionFormatException] should be thrownBy {
      VersionFactory(ExtensionDef.fromOrdered[Maven], extensionParser) parseVersion "1.0.0.0"
    }
  }
}
