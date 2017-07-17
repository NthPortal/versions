package com.nthportal.versions
package variable

final case class ExtendedVersion[E](version: Version, extension: E, protected val extensionDef: ExtensionDef[E])
  extends ExtendedVersionBase[Version, E, ExtendedVersion]

object ExtendedVersion extends ExtendedVersionCompanion[Version, ExtendedVersion](Version) {
  def ofSize(range: Range): ExtendedVersionCompanion[Version, ExtendedVersion] = From(Version.ofSize(range))

  private[variable] final case class From(ofSize: OfSize)
    extends ExtendedVersionCompanion[Version, ExtendedVersion](ofSize) {
    override def apply[E](version: Version, extension: E, ed: ExtensionDef[E]) = ExtendedVersion(version, extension, ed)
  }

}
