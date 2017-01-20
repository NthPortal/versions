package com.nthportal.versions

import scala.language.higherKinds

private[versions] trait VersionBase[V <: VersionBase[V, EV], EV[E] <: ExtendedVersionBase[V, E, EV]]
  extends Ordered[V]
          with Dash[V, EV]
