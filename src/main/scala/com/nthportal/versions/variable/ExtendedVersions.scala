package com.nthportal.versions
package variable

/**
  * Utility object for creating [[ExtendedVersionCompanion companions]]
  * for [[ExtendedVersion]]s which only allow creating versions whose sizes
  * are within a specified range.
  */
object ExtendedVersions {

  private[variable] final case class From(ofSize: Versions.OfSize)
    extends ExtendedVersionCompanion[Version, ExtendedVersion](ofSize) {
    override def apply[E](version: Version, extension: E, ed: ExtensionDef[E]) = ExtendedVersion(version, extension, ed)
  }

  /**
    * Returns a [[ExtendedVersionCompanion companion]] for [[ExtendedVersion]]s
    * which only allows creating versions whose [[Version.size sizes]] are
    * within a specified range.
    *
    * @param range the range in which versions' sizes must be
    * @return a companion for ExtendedVersions which only allows creating
    *         versions whose sizes are within a specified range
    */
  def ofSize(range: Range): ExtendedVersionCompanion[Version, ExtendedVersion] = From(Versions.ofSize(range))
}
