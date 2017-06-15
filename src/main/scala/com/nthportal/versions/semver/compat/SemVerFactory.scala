package com.nthportal.versions
package semver
package compat

import com.nthportal.versions.v3.compat.VersionFactory

/**
  * A factory for for parsing version strings into SemVer versions.
  *
  * @param factory the [[VersionFactory]] with which to parse the version
  *                and its extension
  * @tparam E the type of the versions' extension
  */
@deprecated("use `SemanticVersionFactory` or `SemVerWithoutMetadataFactory` instead", since = "1.3.0")
case class SemVerFactory[E](factory: VersionFactory[E]) {
  /**
    * Parses a version string into a SemVer version. Strips build metadata
    * from the version string if present.
    *
    * @param version the version string to parse
    * @throws VersionFormatException if the given string is not a valid SemVer version
    * @return the SemVer version represented by the specified version
    * @see [[parseSemVerVersion]]
    */
  @throws[VersionFormatException]
  def parseVersion(version: String): v3.ExtendedVersion[E] = {
    parseSemVerVersion(version)(factory.parser, factory.extensionDef)
  }
}

@deprecated("use `SemanticVersionFactory` or `SemVerWithoutMetadataFactory` instead", since = "1.3.0")
object SemVerFactory
