package com.nthportal.versions

import scala.language.higherKinds

private[versions] trait ExtendedVersionBase[V <: VersionBase[V, EV], E, EV[X] <: ExtendedVersionBase[V, X, EV]]
