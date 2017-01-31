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

  override private[versions] def extendedCompanion = ExtendedVersion

  override def toString = s"$major.$minor"
}

object Version extends VersionCompanion[Version, ExtendedVersion] with Of[Dot[Version]] {
  override private[versions] val ordering: Ordering[Version] = Ordering by (v => (v.major, v.minor))

  override def of(major: Int): Dot[Version] = _dot(minor => apply(major, minor))

  override protected def versionFromArray = {case Array(major, minor) => apply(major, minor)}
}
