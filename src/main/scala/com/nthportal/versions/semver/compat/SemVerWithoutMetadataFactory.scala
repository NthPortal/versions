package com.nthportal.versions
package semver
package compat

import com.nthportal.convert.Convert
import com.nthportal.versions.v3.compat.VersionFactory

/**
  * A factory for for parsing version strings into SemVer versions without
  * build metadata.
  *
  * @param factory the [[VersionFactory]] with which to parse the version
  *                and its extension
  * @tparam E the type of the versions' extension
  */
final case class SemVerWithoutMetadataFactory[E](factory: VersionFactory[E]) {
  /**
    * Parses a SemVer version string into a [[v3.Version version]].
    * The version string must not contain build metadata.
    *
    * @param version the version string to parse
    * @throws VersionFormatException if the given string is not a valid SemVer
    *                                version or contains build metadata
    * @return the SemVer version represented by the specified version string
    * @see [[parseSemVerWithoutMetadata]]
    */
  @throws[VersionFormatException]("if the given string is not a valid SemVer version or contains build metadata")
  def parseVersion(version: String): v3.ExtendedVersion[E] = {
    parseSemVerWithoutMetadata(version)(Convert.Valid, factory.extensionDef, factory.parser)
  }
}
