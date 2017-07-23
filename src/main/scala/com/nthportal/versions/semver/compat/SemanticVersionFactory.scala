package com.nthportal.versions
package semver
package compat

import com.nthportal.versions.v3.compat.VersionFactory

/**
  * A factory for for parsing version strings into [[SemanticVersion SemVer versions]]..
  *
  * @param factory the [[VersionFactory]] with which to parse the version
  *                and its extension
  * @param parser  the [[BuildMetadata.Parser]] with which to parse the build
  *                metadata
  * @tparam E the type of the versions' extension
  * @tparam M the type of the build metadata
  */
final case class SemanticVersionFactory[E, M](factory: VersionFactory[E], parser: BuildMetadata.Parser[M]) {
  /**
    * Parses a version string into a SemVer version. Keeps and parses build metadata
    * if present.
    *
    * @param version the version string to parse
    * @throws VersionFormatException if the given string is not a valid SemVer version
    * @return the SemVer version represented by the specified version
    */
  @throws[VersionFormatException]("if the given string is not a valid SemVer version")
  def parseVersion(version: String): SemanticVersion[E, M] = {
    parseSemVer(version)(factory.extensionDef, factory.parser, parser)
  }
}
