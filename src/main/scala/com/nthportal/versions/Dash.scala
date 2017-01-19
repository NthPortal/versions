package com.nthportal.versions

import scala.language.higherKinds

trait Dash[V <: VersionBase[V, EV], EV[E] <: ExtendedVersionBase[V, E, EV]] {
  def dash[E](extension: E)(implicit ed: ExtensionDef[E]): EV[E]

  final def :-[E](extension: E)(implicit ed: ExtensionDef[E]): EV[E] = dash(extension)
}
