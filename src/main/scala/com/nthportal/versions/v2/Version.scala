package com.nthportal.versions
package v2

case class Version(major: Int, minor: Int) extends Ordered[Version] with Dash[ExtendedVersion] {
  // Validate values
  require(major >= 0 && minor >= 0, "major and minor values must be >= 0")

  override def dash[E](extension: E)(implicit ed: ExtensionDef[E]): ExtendedVersion[E] = {
    ExtendedVersion(this, extension, ed)
  }

  override def compare(that: Version): Int = Version.ordering.compare(this, that)

  override def toString = s"$major.$minor"
}

object Version {
  private val ordering: Ordering[Version] = Ordering by (v => (v.major, v.minor))

  def apply(major: Int): Dot[Version] = (minor: Int) => apply(major, minor)

  def :>(major: Int): Dot[Version] = apply(major)

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
