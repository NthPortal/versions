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
    * Parses a string into an extended version.
    *
    * @param v the string to parse
    * @throws VersionFormatException if the given string is not a valid extended version
    * @return the extended version represented by the string
    */
  @throws[VersionFormatException]
  def parseVersion[E](v: String)(implicit ed: ExtensionDef[E], ep: ExtensionParser[E]): EV[E] = {
    v.split("-", 2) match {
      case Array(version, extension) =>
        try {
          c.parseVersion(version) :- ep.parse(extension)
        } catch {
          case e@(_: IllegalArgumentException | _: VersionFormatException) => throw new VersionFormatException(v, e)
        }
      case Array(version) => ed.default match {
        case Some(extension) => c.parseVersion(version) :- extension
        case None => throw new VersionFormatException(v, new UnsupportedOperationException("No default extension"))
      }
    }
  }
}
