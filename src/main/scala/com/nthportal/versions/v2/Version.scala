package com.nthportal
package versions
package v2

import com.nthportal.convert.Convert

/**
  * A version of the form `major`.`minor` (such as, for example, `1.3`).
  *
  * @param major the major version number
  * @param minor the minor version number
  */
final case class Version(major: Int, minor: Int) extends VersionBase[Version, ExtendedVersion] {
  // Validate values
  require(major >= 0 && minor >= 0, "major and minor values must be >= 0")

  override protected def companion = Version

  override protected[versions] def extendedCompanion = ExtendedVersion

  override def toSeq = Seq(major, minor)

  override def toString = s"$major.$minor"
}

object Version extends VersionCompanion[Version, ExtendedVersion] with Of[Dot[Version]] {
  override private[versions] val ordering: Ordering[Version] =
    Ordering.by[Version, Int](_.major)
      .thenBy(_.minor)

  override def of(major: Int): Dot[Version] = minor => apply(major, minor)

  override protected[versions] def versionFromSeq = { case Seq(major, minor) => apply(major, minor) }

  /**
    * Extracts a version from a string.
    *
    * @param version the string from which to extract a version
    * @return an [[scala.Option Option]] containing the major and minor version numbers;
    *         [[scala.None None]] if the string did not represent a valid version
    */
  def unapply(version: String): Option[(Int, Int)] = parseVersion(version)(Convert.Any) flatMap unapply
}
