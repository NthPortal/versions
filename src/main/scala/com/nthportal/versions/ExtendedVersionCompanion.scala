package com.nthportal.versions

import com.nthportal.convert.Convert

import scala.language.higherKinds

/**
  * A companion object for an extended version.
  *
  * @param companion a [[VersionCompanion]] for the version component of the
  *                  extended version
  * @tparam V  the type of the version component of the extended version
  * @tparam EV the type of the extended version
  */
abstract class ExtendedVersionCompanion[V <: VersionBase[V, EV], EV[E] <: ExtendedVersionBase[V, E, EV]]
(companion: VersionCompanion[V, EV]) {
  /**
    * Returns the [[VersionCompanion companion object]] for
    * non-extended versions.
    *
    * @return the companion object for non-extended versions
    */
  private[versions] final def baseCompanion: VersionCompanion[V, EV] = companion

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
    * @param c       the `Convert` to use
    * @param ed      the [[ExtensionDef extension definition]]
    * @param ep      a [[ExtensionParser parser]] for extensions
    * @tparam E the type of the extension
    * @throws VersionFormatException if the given string is not a valid extended version
    *                                (when `c` is `Convert.Valid`)
    * @return the extended version represented by the string
    */
  @throws[VersionFormatException]("if the given string is not a valid extended version")
  def parseVersion[E](version: String)
                     (implicit c: Convert,
                      ed: ExtensionDef[E],
                      ep: ExtensionParser[E]): c.Result[EV[E]] = {
    import c._
    conversion {
      import AutoUnwrap._
      version.split("-", 2) match {
        case Array(ver, extension) =>
          try {
            companion.parseVersion(ver) -- ep.parse(extension)
          } catch {
            case e: IllegalArgumentException => fail(new VersionFormatException(version, e))
          }
        case Array(ver) => ed.default match {
          case Some(extension) => companion.parseVersion(ver) -- extension
          case None =>
            fail(new VersionFormatException(version, new UnsupportedOperationException("No default extension")))
        }
      }
    }
  }

  /**
    * Extracts an extended version from a version string.
    *
    * @param version the string from which to extract an extended version
    * @param ed      the [[ExtensionDef extension definition]]
    * @param ep      a [[ExtensionParser parser]] for extensions
    * @tparam E the type of the extension
    * @return an [[scala.Option Option]] containing the version and extension represented
    *         by the string; [[scala.None None]] if the string did not represent a valid
    *         extended version
    */
  def unapply[E](version: String)(implicit ed: ExtensionDef[E], ep: ExtensionParser[E]): Option[(V, E)] = {
    import Convert.AsOption.Implicit.ref
    parseVersion(version) map { ev => (ev.version, ev.extension) }
  }
}
