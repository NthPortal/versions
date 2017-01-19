package com.nthportal.versions
package v2

case class ExtendedVersion[E](version: Version, extension: E, protected val extensionDef: ExtensionDef[E])
  extends Ordered[ExtendedVersion[E]] {
  override def compare(that: ExtendedVersion[E]): Int = extensionDef.ordering2.compare(this, that)

  override def toString = s"$version${extensionDef.extToString(extension)}"
}

object ExtendedVersion {
  def parseVersion[E](v: String)(implicit ed: ExtensionDef[E], ep: ExtensionParser[E]): ExtendedVersion[E] = {
    v.split("-", 2) match {
      case Array(version, extension) =>
        try {
          Version.parseVersion(version) :- ep.parse(extension)
        } catch {
          case e@(_: IllegalArgumentException | _: VersionFormatException) => throw new VersionFormatException(v, e)
        }
      case Array(version) => ed.default match {
        case Some(extension) => Version.parseVersion(version) :- extension
        case None => throw new VersionFormatException(v, new UnsupportedOperationException("No default extension"))
      }
    }
  }
}
