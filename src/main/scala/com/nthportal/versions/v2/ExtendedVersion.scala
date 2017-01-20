package com.nthportal.versions
package v2

case class ExtendedVersion[E](version: Version, extension: E, protected val extensionDef: ExtensionDef[E])
  extends ExtendedVersionBase[Version, E, ExtendedVersion] {
  override def compare(that: ExtendedVersion[E]): Int = extensionDef.ordering2.compare(this, that)

  override def toString = s"$version${extensionDef.extToString(extension)}"
}

object ExtendedVersion extends ExtendedVersionCompanion[Version, ExtendedVersion](Version)
