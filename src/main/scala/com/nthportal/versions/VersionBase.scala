package com.nthportal.versions

import scala.language.higherKinds

private[versions] trait VersionBase[V <: VersionBase[V, EV], EV[E] <: ExtendedVersionBase[V, E, EV]]
  extends Ordered[V]
          with Dash[V, EV] {
  def companion: VersionCompanion[V, EV]

  override def compare(that: V) = companion.ordering.compare(this.asInstanceOf[V], that)
}
