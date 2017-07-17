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
abstract class ExtendedVersionCompanion[V <: VersionBase[V, EV], EV[E] <: ExtendedVersionBase[V, E, EV]]
(c: VersionCompanion[V, EV]) {
  /**
    * Returns the [[VersionCompanion companion object]] for
    * non-extended versions.
    *
    * @return the companion object for non-extended versions
    */
  final def baseCompanion: VersionCompanion[V, EV] = c

  /**
    * Creates an extended version from a version, extension, and
    * [[ExtensionDef extension definition]].
    *
    * @param version   the version component of the extended version
    * @param extension the extension component of the extended version
    * @param ed        the extension definition for the version's extension
    * @tparam E the type of the extension
    * @return an extended version with the specified parameters
    */
  def apply[E](version: V, extension: E, ed: ExtensionDef[E]): EV[E]

  /**
    * Parses a string into an extended version.
    *
    * @param version the string to parse
    * @param ed      the [[ExtensionDef extension definition]]
    * @param ep      a [[ExtensionParser parser]] for extensions
    * @tparam E the type of the extension
    * @throws VersionFormatException if the given string is not a valid extended version
    * @return the extended version represented by the string
    */
  @throws[VersionFormatException]
  def parseVersion[E](@deprecatedName('v, since = "1.3.0") version: String)
                     (implicit ed: ExtensionDef[E],
                      ep: ExtensionParser[E]): EV[E] = {
    version.split("-", 2) match {
      case Array(ver, extension) =>
        try {
          c.parseVersion(ver) -- ep.parse(extension)
        } catch {
          case e: IllegalArgumentException => throw new VersionFormatException(version, e)
        }
      case Array(ver) => ed.default match {
        case Some(extension) => c.parseVersion(ver) -- extension
        case None => throw new VersionFormatException(version, new UnsupportedOperationException("No default extension"))
      }
    }
  }

  /**
    * Parses a string into an extended version.
    *
    * @param version the string to parse
    * @param ed      the [[ExtensionDef extension definition]]
    * @param ep      a [[ExtensionParser parser]] for extensions
    * @tparam E the type of the extension
    * @throws VersionFormatException if the given string is not a valid extended version
    * @return an [[Option]] containing the extended version represented by the string;
    *         [[None]] if the string did not represent a valid extended version
    */
  def parseAsOption[E](version: String)(implicit ed: ExtensionDef[E], ep: ExtensionParser[E]): Option[EV[E]] = {
    formatCheckToOption { parseVersion(version) }
  }

  /**
    * Extracts an extended version from a version string.
    *
    * @param version the string from which to extract an extended version
    * @param ed      the [[ExtensionDef extension definition]]
    * @param ep      a [[ExtensionParser parser]] for extensions
    * @tparam E the type of the extension
    * @return an [[Option]] containing the version and extension represented
    *         by the string; [[None]] if the string did not represent a valid
    *         extended version
    */
  def unapply[E](version: String)(implicit ed: ExtensionDef[E], ep: ExtensionParser[E]): Option[(V, E)] = {
     parseAsOption(version) map { ev => (ev.version, ev.extension) }
  }
}
