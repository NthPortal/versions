package com.nthportal.versions

import scala.language.higherKinds

/**
  * A companion object for an extended version.
  *
  * @param c a [[VersionCompanion]] for the version component of the
  *          extended version
  * @tparam V  the type of the version component of the extended version
  * @tparam EV the type of the extended version
  */
private[versions]
abstract class ExtendedVersionCompanion[V <: VersionBase[V, EV], EV[E] <: ExtendedVersionBase[V, E, EV]]
(c: VersionCompanion[V, EV]) {
  /**
    * Creates an extended version from a version, extension, and
    * [[ExtensionDef extension definition]].
    *
    * @param version the version component of the extended version
    * @param extension the extension component of the extended version
    * @param ed the extension definition for the version's extension
    * @tparam E the type of the extension
    * @return an extended version with the specified parameters
    */
  def apply[E](version: V, extension: E, ed: ExtensionDef[E]): EV[E]

  /**
    * Parses a string into an extended version.
    *
    * @param v  the string to parse
    * @param ed the [[ExtensionDef extension definition]]
    * @param ep a [[ExtensionParser parser]] for extensions
    * @tparam E the type of the extension
    * @throws VersionFormatException if the given string is not a valid extended version
    * @return the extended version represented by the string
    */
  @throws[VersionFormatException]
  def parseVersion[E](v: String)(implicit ed: ExtensionDef[E], ep: ExtensionParser[E]): EV[E] = {
    v.split("-", 2) match {
      case Array(version, extension) =>
        try {
          c.parseVersion(version) -- ep.parse(extension)
        } catch {
          case e@(_: IllegalArgumentException | _: VersionFormatException) => throw new VersionFormatException(v, e)
        }
      case Array(version) => ed.default match {
        case Some(extension) => c.parseVersion(version) -- extension
        case None => throw new VersionFormatException(v, new UnsupportedOperationException("No default extension"))
      }
    }
  }
}
