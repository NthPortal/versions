package com.nthportal.versions
package v2

case class ExtendedVersion[E](version: Version, extension: E, protected val extensionDef: ExtensionDef[E])
  extends ExtendedVersionBase[Version, E, ExtendedVersion](_.ordering2)

object ExtendedVersion extends ExtendedVersionCompanion[Version, ExtendedVersion](Version)
