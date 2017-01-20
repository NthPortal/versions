package com.nthportal.versions
package v3

case class ExtendedVersion[E](version: Version, extension: E, protected val extensionDef: ExtensionDef[E])
  extends ExtendedVersionBase[Version, E, ExtendedVersion] {
  override def compare(that: ExtendedVersion[E]): Int = extensionDef.ordering3.compare(this, that)

  override def toString = s"$version${extensionDef.extToString(extension)}"
}

object ExtendedVersion extends ExtendedVersionCompanion[Version, ExtendedVersion](Version)
