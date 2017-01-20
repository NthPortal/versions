package com.nthportal.versions
package v3
package compat

import com.nthportal.versions.compat.VersionFactoryBase

/**
  * A version factory for [[ExtendedVersion]]s.
  *
  * @inheritdoc
  */
case class VersionFactory[E](extensionDef: ExtensionDef[E], parser: ExtensionParser[E])
  extends VersionFactoryBase[Version, E, ExtendedVersion](extensionDef, parser, ExtendedVersion)
