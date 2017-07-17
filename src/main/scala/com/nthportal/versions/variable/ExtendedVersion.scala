package com.nthportal.versions
package variable

final case class ExtendedVersion[E](version: Version, extension: E, protected val extensionDef: ExtensionDef[E])
  extends ExtendedVersionBase[Version, E, ExtendedVersion]

object ExtendedVersion extends ExtendedVersionCompanion[Version, ExtendedVersion](Version) {
  def withSize(range: Range): ExtendedVersionCompanion[Version, ExtendedVersion] = From(Version.withSize(range))

  private final case class From(companion: WithSize)
    extends ExtendedVersionCompanion[Version, ExtendedVersion](companion) {
    override def apply[E](version: Version, extension: E, ed: ExtensionDef[E]) = ExtendedVersion(version, extension, ed)
  }

}
