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

  override def companion = Version

  /**
    * Creates an [[ExtendedVersion]] from this version with the specified extension.
    *
    * @param extension the extension for the ExtendedVersion
    * @param ed        the [[ExtensionDef extension definition]]
    * @tparam E the type of the extension
    * @return an ExtendedVersion with this version and the specified extension
    */
  override def dash[E](extension: E)(implicit ed: ExtensionDef[E]): ExtendedVersion[E] = {
    ExtendedVersion(this, extension, ed)
  }

  override def toString = s"$major.$minor"
}

object Version extends VersionCompanion[Version, ExtendedVersion] with Of[Dot[Version]] {
  override private[versions] val ordering: Ordering[Version] = Ordering by (v => (v.major, v.minor))

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
