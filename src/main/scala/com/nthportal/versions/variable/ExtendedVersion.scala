package com.nthportal.versions
package variable

/**
  * A version with an extension (such as, for example, `1.2.5.4.1632-SNAPSHOT`)
  *
  * @param version      the version component of this extended version
  * @param extension    the extension component of this extended version
  * @param extensionDef the [[ExtensionDef extension definition]] for this version's extension
  * @tparam E the type of the extension component of this extended version
  */
final case class ExtendedVersion[E](version: Version, extension: E, protected val extensionDef: ExtensionDef[E])
  extends ExtendedVersionBase[Version, E, ExtendedVersion]

object ExtendedVersion extends ExtendedVersionCompanion[Version, ExtendedVersion](Version) {
  private[variable] final case class OfSize(ofSize: Version.OfSize)
    extends ExtendedVersionCompanion[Version, ExtendedVersion](ofSize) {

    @throws[IllegalArgumentException]("if the number of values in the version does not conform to the allowed sizes")
    override def apply[E](version: Version, extension: E, ed: ExtensionDef[E]) = {
      ofSize.checkSize(version.values)
      ExtendedVersion(version, extension, ed)
    }
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
  def ofSize(range: Range): ExtendedVersionCompanion[Version, ExtendedVersion] = OfSize(Version.ofSize(range))
}
