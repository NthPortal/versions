package com.nthportal.versions

import scala.language.higherKinds

/**
  * Base trait for a version
  *
  * @tparam V  the type of the version
  * @tparam EV the type of the extended version associated with the version
  */
private[versions] trait VersionBase[V <: VersionBase[V, EV], EV[E] <: ExtendedVersionBase[V, E, EV]]
  extends Ordered[V]
          with Dash[V, EV] {
  /**
    * Returns the [[VersionCompanion companion object]] for this version.
    *
    * @return the companion object for this version
    */
  private[versions] def companion: VersionCompanion[V, EV]

  override def dash[E](extension: E)(implicit ed: ExtensionDef[E]) = {
    companion.extendedVersionCompanion(this.asInstanceOf[V], extension, ed)
  }

  override def compare(that: V) = companion.ordering.compare(this.asInstanceOf[V], that)
}
