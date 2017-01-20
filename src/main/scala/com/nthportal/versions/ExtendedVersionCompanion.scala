package com.nthportal.versions

import scala.language.higherKinds

private[versions]
abstract class ExtendedVersionCompanion[V <: VersionBase[V, EV], EV[E] <: ExtendedVersionBase[V, E, EV]]
(c: VersionCompanion[V, EV]) {
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
