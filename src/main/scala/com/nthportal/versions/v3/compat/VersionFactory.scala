package com.nthportal.versions
package v3
package compat

import com.nthportal.versions.compat.VersionFactoryBase

/**
  * A version factory for [[ExtendedVersion]]s.
  *
  * @param extensionDef the [[ExtensionDef extension definition]]
  * @param parser       a [[ExtensionParser parser]] for extensions
  * @tparam E the type of the extension component of the extended version
  */
case class VersionFactory[E](extensionDef: ExtensionDef[E], parser: ExtensionParser[E])
  extends VersionFactoryBase[Version, E, ExtendedVersion](extensionDef, parser, ExtendedVersion)
