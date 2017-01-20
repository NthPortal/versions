package com.nthportal.versions
package v3

case class ExtendedVersion[E](version: Version, extension: E, protected val extensionDef: ExtensionDef[E])
  extends ExtendedVersionBase[Version, E, ExtendedVersion]

object ExtendedVersion extends ExtendedVersionCompanion[Version, ExtendedVersion](Version)
