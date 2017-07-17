package com.nthportal.versions
package variable

final case class ExtendedVersion[E](version: Version, extension: E, protected val extensionDef: ExtensionDef[E])
  extends ExtendedVersionBase[Version, E, ExtendedVersion]

object ExtendedVersion extends ExtendedVersionCompanion[Version, ExtendedVersion](Version)
