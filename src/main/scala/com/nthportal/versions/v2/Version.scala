package com.nthportal.versions
package v2

case class Version(major: Int, minor: Int) extends VersionBase[Version, ExtendedVersion] {
  // Validate values
  require(major >= 0 && minor >= 0, "major and minor values must be >= 0")

  override def dash[E](extension: E)(implicit ed: ExtensionDef[E]): ExtendedVersion[E] = {
    ExtendedVersion(this, extension, ed)
  }

  override def compare(that: Version): Int = Version.ordering.compare(this, that)

  override def toString = s"$major.$minor"
}

object Version extends VersionCompanion[Version, ExtendedVersion] with Of[Dot[Version]] {
  private val ordering: Ordering[Version] = Ordering by (v => (v.major, v.minor))

  override def of(major: Int): Dot[Version] = minor => apply(major, minor)

  @throws[VersionFormatException]
  def parseVersion(v: String): Version = {
    v split '.' match {
      case Array(major, minor) =>
        try {
          apply(Integer.parseInt(major), Integer.parseInt(minor))
        } catch {
          case e@(_: IllegalArgumentException | _: NumberFormatException) => throw new VersionFormatException(v, e)
        }
      case _ => throw new VersionFormatException(v)
    }
  }
}
