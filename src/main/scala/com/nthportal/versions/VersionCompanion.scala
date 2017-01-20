package com.nthportal.versions

import scala.language.higherKinds

/**
  * A companion object for a version.
  *
  * @tparam V the type of the version
  * @tparam EV the type of the extended version associated with the version
  */
private[versions] trait VersionCompanion[V <: VersionBase[V, EV], EV[E] <: ExtendedVersionBase[V, E, EV]] {
  /**
    * Parses a string into a version.
    *
    * @param v the string to parse
    * @throws VersionFormatException if the given string is not a valid version
    * @return the version represented by the string
    */
  @throws[VersionFormatException]
  def parseVersion(v: String): V

  /**
    * An ordering for the version for which this is a companion.
    */
  private[versions] val ordering: Ordering[V]
}
