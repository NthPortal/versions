package com.nthportal.versions
package v3

case class Version(major: Int, minor: Int, patch: Int) extends Ordered[Version] with Dash[ExtendedVersion] {
  // Validate values
  require(major >= 0 && minor >= 0 && patch >= 0, "major, minor, and patch values must all be >= 0")

  override def dash[E](extension: E)(implicit ed: ExtensionDef[E]): ExtendedVersion[E] = {
    ExtendedVersion(this, extension, ed)
  }

  override def compare(that: Version): Int = Version.ordering.compare(this, that)

  override def toString = s"$major.$minor.$patch"
}

object Version {
  private val ordering: Ordering[Version] = Ordering by (v => (v.major, v.minor, v.patch))

  def apply(major: Int): Dot[Dot[Version]] = (minor: Int) => (patch: Int) => apply(major, minor, patch)

  def :>(major: Int): Dot[Dot[Version]] = apply(major)

  def parseVersion(v: String): Version = {
    v split '.' match {
      case Array(major, minor, patch) =>
        try {
          apply(Integer.parseInt(major), Integer.parseInt(minor), Integer.parseInt(patch))
        } catch {
          case e@(_: IllegalArgumentException | _: NumberFormatException) => throw new VersionFormatException(v, e)
        }
      case _ => throw new VersionFormatException(v)
    }
  }
}
