package com.nthportal.versions
package variable

object ExtendedVersions {

  private[variable] final case class From(ofSize: Versions.OfSize)
    extends ExtendedVersionCompanion[Version, ExtendedVersion](ofSize) {
    override def apply[E](version: Version, extension: E, ed: ExtensionDef[E]) = ExtendedVersion(version, extension, ed)
  }

  def ofSize(range: Range): ExtendedVersionCompanion[Version, ExtendedVersion] = From(Versions.ofSize(range))
}
