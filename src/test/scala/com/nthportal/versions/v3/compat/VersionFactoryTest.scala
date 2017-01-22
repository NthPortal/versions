package com.nthportal.versions
package v3
package compat

import com.nthportal.versions.extensions.Maven
import com.nthportal.versions.extensions.Maven._

class VersionFactoryTest extends SimpleSpec {

  behavior of "VersionFactory (3)"

  it should "parse versions correctly" in {
    val f = VersionFactory(extensionDef, parser)

    f parseVersion "1.2.5" should equal(Version ⋮ 1⋅2⋅5 -- Release)
    f parseVersion "0.0.0-SNAPSHOT" should equal(Version ⋮ 0⋅0⋅0 -- Snapshot)

    a [VersionFormatException] should be thrownBy {f parseVersion "1.0.0-INVALID"}
    a [VersionFormatException] should be thrownBy {f parseVersion "1.0.0-RELEASE"}
    a [VersionFormatException] should be thrownBy {f parseVersion "1.0.0-snapshot"}
    a [VersionFormatException] should be thrownBy {f parseVersion "1.0.0-SNAPSHOT-4"}
    a [VersionFormatException] should be thrownBy {f parseVersion "really not a version"}

    // Missing default extension
    a [VersionFormatException] should be thrownBy {
      VersionFactory(ExtensionDef.fromOrdered[Maven], parser) parseVersion "1.0.0"
    }
  }
}
