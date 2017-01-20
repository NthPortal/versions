package com.nthportal.versions

import scala.language.higherKinds

private[versions] trait ExtendedVersionBase[V <: VersionBase[V, EV], E, EV[X] <: ExtendedVersionBase[V, X, EV]]
  extends Ordered[EV[E]] {
  val version: V
  val extension: E
  protected val extensionDef: ExtensionDef[E]

  private implicit def eOrd = extensionDef.ordering

  override def compare(that: EV[E]): Int = {
    implicitly[Ordering[(V, E)]].compare((this.version, this.extension), (that.version, that.extension))
  }

  override def toString = s"$version${extensionDef.extToString(extension)}"
}
