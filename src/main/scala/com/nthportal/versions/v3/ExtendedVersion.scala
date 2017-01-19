package com.nthportal.versions
package v3

case class ExtendedVersion[E](version: Version, extension: E, protected val extensionDef: ExtensionDef[E])
  extends Ordered[ExtendedVersion[E]] {
  override def compare(that: ExtendedVersion[E]): Int = extensionDef.ordering3.compare(this, that)
}

object ExtendedVersion {
  def parseVersion[E](v: String)(implicit epd: ExtensionParseDef[E]): ExtendedVersion[E] = {
    implicit def eDef = epd.extensionDef

    v.split("-", 2) match {
      case Array(version, extension) =>
        try {
          Version.parseVersion(version) :- epd.parser.parse(extension)
        } catch {
          case e@(_: IllegalArgumentException | _: VersionFormatException) => throw new VersionFormatException(v, e)
        }
      case Array(version) => epd.extensionDef.default match {
        case Some(extension) => Version.parseVersion(version) :- extension
        case None => throw new VersionFormatException(v, new UnsupportedOperationException("No default extension"))
      }
    }
  }
}
