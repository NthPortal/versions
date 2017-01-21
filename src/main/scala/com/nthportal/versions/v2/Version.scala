package com.nthportal.versions
package v2

/**
  * A version of the form `major`.`minor` (such as, for example, `1.3`).
  *
  * @param major the major version number
  * @param minor the minor version number
  */
case class Version(major: Int, minor: Int) extends VersionBase[Version, ExtendedVersion] {
  // Validate values
  require(major >= 0 && minor >= 0, "major and minor values must be >= 0")

  override private[versions] def companion = Version

  override def toString = s"$major.$minor"
}

object Version extends VersionCompanion[Version, ExtendedVersion] with Of[Dot[Version]] {
  override private[versions] val ordering: Ordering[Version] = Ordering by (v => (v.major, v.minor))

  override private[versions] def extendedVersionCompanion = ExtendedVersion

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
