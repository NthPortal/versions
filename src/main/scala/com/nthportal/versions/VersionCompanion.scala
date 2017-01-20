package com.nthportal.versions

import scala.language.higherKinds

private[versions] trait VersionCompanion[V <: VersionBase[V, EV], EV[E] <: ExtendedVersionBase[V, E, EV]] {
  @throws[VersionFormatException]
  def parseVersion(v: String): V
}
