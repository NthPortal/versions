package com.nthportal.versions

import scala.language.higherKinds

/**
  * Indicates that an extension can be appended to this to create an extended version.
  *
  * @tparam V  the type of the version component of the extended version
  * @tparam EV the type of the extended version
  */
trait Dash[V <: VersionBase[V, EV], EV[E] <: ExtendedVersionBase[V, E, EV]] {
  /**
    * Creates an extended version from this version with the specified extension.
    *
    * @param extension the extension for the ExtendedVersion
    * @param ed        the [[ExtensionDef extension definition]]
    * @tparam E the type of the extension
    * @return an extended version with this version and the specified extension
    */
  def dash[E](extension: E)(implicit ed: ExtensionDef[E]): EV[E]

  /**
    * @see [[dash]]
    */
  final def :-[E](extension: E)(implicit ed: ExtensionDef[E]): EV[E] = dash(extension)
}
