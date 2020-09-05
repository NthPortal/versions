package com.nthportal.versions
package compat

import scala.language.higherKinds

/**
 * Base class for (extended) version factories.
 *
 * @param extensionDef the [[ExtensionDef extension definition]]
 * @param parser       a [[ExtensionParser parser]] for extensions
 * @param companion    a companion for the extended version
 * @tparam V  the type of the version component of the extended version
 * @tparam E  the type of the extension component of the extended version
 * @tparam EV the type of the extended version
 */
private[versions] abstract class VersionFactoryBase[V <: VersionBase[V, EV], E, EV[X] <: ExtendedVersionBase[V, X, EV]](
    extensionDef: ExtensionDef[E],
    parser: ExtensionParser[E],
    companion: ExtendedVersionCompanion[V, EV]
) {

  /**
   * Parses a string into an extended version.
   *
   * @param version the string to parse
   * @throws VersionFormatException if the given string is not a valid extended version
   * @return the extended version represented by the string
   */
  def parseVersion(@deprecatedName('v) version: String): EV[E] = companion.parseVersion(version)(extensionDef, parser)
}
