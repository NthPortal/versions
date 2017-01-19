package com.nthportal.versions

private[versions] abstract class ExtendedVersionCompanion[V <: VersionBase[V], EV[E] <: ExtendedVersionBase[V, E]]
(c: VersionCompanion[V]) {
  def apply[E](v: V, e: E, ed: ExtensionDef[E]): EV[E]

  @throws[VersionFormatException]
  def parseVersion[E](v: String)(implicit ed: ExtensionDef[E], ep: ExtensionParser[E]): EV[E] = {
    v.split("-", 2) match {
      case Array(version, extension) =>
        try {
          apply(c.parseVersion(version), ep.parse(extension), ed)
        } catch {
          case e@(_: IllegalArgumentException | _: VersionFormatException) => throw new VersionFormatException(v, e)
        }
      case Array(version) => ed.default match {
        case Some(extension) => apply(c.parseVersion(version), extension, ed)
        case None => throw new VersionFormatException(v, new UnsupportedOperationException("No default extension"))
      }
    }
  }
}
