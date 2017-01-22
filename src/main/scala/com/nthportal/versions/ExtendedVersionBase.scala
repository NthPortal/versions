package com.nthportal.versions

import scala.language.higherKinds

/**
  * Base trait for an extended version.
  *
  * @tparam V  the type of the version component of this extended version
  * @tparam E  the type of the extension component of this extended version
  * @tparam EV the type of this extended version
  */
private[versions] trait ExtendedVersionBase[V <: VersionBase[V, EV], E, EV[X] <: ExtendedVersionBase[V, X, EV]]
  extends Ordered[EV[E]] {
  /**
    * The version component of this extended version.
    */
  val version: V
  /**
    * The extension component of this extended version.
    */
  val extension: E
  /**
    * The [[ExtensionDef extension definition]] for this version's extension.
    */
  protected val extensionDef: ExtensionDef[E]

  private implicit def eOrd = extensionDef.ordering

  override def compare(that: EV[E]): Int = {
    implicitly[Ordering[(V, E)]].compare((this.version, this.extension), (that.version, that.extension))
  }

  override def toString = s"$version${extensionDef.extToString(extension)}"
}
