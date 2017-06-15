package com.nthportal.versions
package semver

import com.nthportal.versions.extensions.Maven._

@deprecated("testing deprecated methods", since = "1.3.0")
class DeprecatedSemVerTest extends SimpleSpec {
  it should "(deprecated) parse versions correctly" in {
    val v = parseSemVerVersion("1.0.0-SNAPSHOT+build.12654")

    v should equal (v3.Version(1)(0)(0) -- Snapshot)
    v should equal (parseSemVerVersion("1.0.0-SNAPSHOT+12654"))
    v should equal (parseSemVerVersion("1.0.0-SNAPSHOT"))

    v should equal (parseSemVerWithBuildMetadata("1.0.0-SNAPSHOT+build.12654").extendedVersion)

    a [VersionFormatException] should be thrownBy {parseSemVerVersion("1.0.0-SNAPSHOT+")}
    a [VersionFormatException] should be thrownBy {parseSemVerVersion("1.0.0-SNAPSHOT+build+12654")}
  }

  it should "(deprecated) only allow valid SemVer extensions and metadata" in {
    implicit val extensionParser: ExtensionParser[String] = identity(_)
    implicit val extensionDef: ExtensionDef[String] = ExtensionDef(None, _ compare _)

    a [VersionFormatException] should be thrownBy {parseSemVerVersion("1.0.0-SNAP_SHOT")}
    a [VersionFormatException] should be thrownBy {parseSemVerVersion("1.0.0-SNAP..SHOT")}
    a [VersionFormatException] should be thrownBy {parseSemVerVersion("1.0.0-SNAPSHOT?")}
    a [VersionFormatException] should be thrownBy {parseSemVerVersion("1.0.0-.SNAPSHOT")}
    a [VersionFormatException] should be thrownBy {parseSemVerVersion("1.0.0-SNAP.SHOT.")}

    noException should be thrownBy {parseSemVerVersion("1.0.0-SNAP.SHOT")}
    noException should be thrownBy {parseSemVerVersion("1.0.0-SNAP-SHOT")}
    noException should be thrownBy {parseSemVerVersion("1.0.0--SNAPSHOT")}
    noException should be thrownBy {parseSemVerVersion("1.0.0-SNAP--SHOT")}
    noException should be thrownBy {parseSemVerVersion("1.0.0-SNAPSHOT-")}

    a [VersionFormatException] should be thrownBy {parseSemVerVersion("1.0.0-SNAPSHOT+build..12654")}
    a [VersionFormatException] should be thrownBy {parseSemVerVersion("1.0.0-SNAPSHOT+.build.12654")}
    a [VersionFormatException] should be thrownBy {parseSemVerVersion("1.0.0-SNAPSHOT+build.12654.")}
    a [VersionFormatException] should be thrownBy {parseSemVerVersion("1.0.0-SNAPSHOT+build_12654")}

    noException should be thrownBy {parseSemVerVersion("1.0.0-SNAPSHOT+build.12654")}
    noException should be thrownBy {parseSemVerVersion("1.0.0-SNAPSHOT+build-12654")}
    noException should be thrownBy {parseSemVerVersion("1.0.0-SNAPSHOT+-build.12654")}
    noException should be thrownBy {parseSemVerVersion("1.0.0-SNAPSHOT+build--12654")}
    noException should be thrownBy {parseSemVerVersion("1.0.0-SNAPSHOT+build.12654-")}
  }
}
