package com.nthportal.versions
package compat

import scala.language.higherKinds

private[versions]
abstract class VersionFactoryBase[V <: VersionBase[V, EV], E, EV[X] <: ExtendedVersionBase[V, X, EV]]
(extensionDef: ExtensionDef[E], parser: ExtensionParser[E], companion: ExtendedVersionCompanion[V, EV]) {
  def parseVersion(v: String): EV[E] = companion.parseVersion(v)(extensionDef, parser)
}
