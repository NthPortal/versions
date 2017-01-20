package com.nthportal.versions
package v2
package compat

import com.nthportal.versions.compat.VersionFactoryBase

case class VersionFactory[E](extensionDef: ExtensionDef[E], parser: ExtensionParser[E])
  extends VersionFactoryBase[Version, E, ExtendedVersion](extensionDef, parser, ExtendedVersion)
