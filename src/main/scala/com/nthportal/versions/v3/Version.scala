package com.nthportal
package versions
package v3

import com.nthportal.convert.Convert

/**
  * A version of the form `major`.`minor`.`patch` (such as, for example, `1.2.5`).
  *
  * This type of version may or may not conform to the [[http://semver.org/ SemVer]]
  * specification.
  *
  * @param major the major version number
  * @param minor the minor version number
  * @param patch the patch version number
  */
final case class Version(major: Int, minor: Int, patch: Int) extends VersionBase[Version, ExtendedVersion] {
  // Validate values
  require(major >= 0 && minor >= 0 && patch >= 0, "major, minor, and patch values must all be >= 0")

  override protected def companion = Version

  override protected[versions] def extendedCompanion = ExtendedVersion

  override def toSeq = Seq(major, minor, patch)

  override def toString = s"$major.$minor.$patch"
}

object Version extends VersionCompanion[Version, ExtendedVersion] with Of[Dot[Dot[Version]]] {
  override private[versions] val ordering: Ordering[Version] =
    Ordering.by[Version, Int](_.major)
      .thenBy(_.minor)
      .thenBy(_.patch)

  override def of(major: Int): Dot[Dot[Version]] = minor => patch => apply(major, minor, patch)

  override protected[versions] def versionFromSeq = { case Seq(major, minor, patch) => apply(major, minor, patch) }

  /**
    * Extracts a version from a string.
    *
    * @param version the string from which to extract a version
    * @return an [[scala.Option Option]] containing the major, minor and patch version numbers;
    *         [[scala.None None]] if the string did not represent a valid version
    */
  def unapply(version: String): Option[(Int, Int, Int)] = parseVersion(version)(Convert.Any) flatMap unapply
}
