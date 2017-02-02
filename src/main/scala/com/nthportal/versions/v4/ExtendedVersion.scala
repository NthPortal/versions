package com.nthportal.versions
package v4

/**
  * A version with an extension (such as, for example, `1.2.5.4-rc.1`)
  *
  * @param version      the version component of this extended version
  * @param extension    the extension component of this extended version
  * @param extensionDef the [[ExtensionDef extension definition]] for this version's extension
  * @tparam E the type of the extension component of this extended version
  */
case class ExtendedVersion[E](version: Version, extension: E, protected val extensionDef: ExtensionDef[E])
  extends ExtendedVersionBase[Version, E, ExtendedVersion]

object ExtendedVersion extends ExtendedVersionCompanion[Version, ExtendedVersion](Version)
