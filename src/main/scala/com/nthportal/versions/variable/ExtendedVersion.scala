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

object ExtendedVersion extends ExtendedVersionCompanion[Version, ExtendedVersion](Version)
