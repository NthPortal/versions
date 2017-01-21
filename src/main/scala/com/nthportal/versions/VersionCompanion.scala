package com.nthportal.versions

import scala.language.higherKinds

/**
  * A companion object for a version.
  *
  * @tparam V  the type of the version
  * @tparam EV the type of the extended version associated with the version
  */
private[versions] trait VersionCompanion[V <: VersionBase[V, EV], EV[E] <: ExtendedVersionBase[V, E, EV]] {
  /**
    * An ordering for the version for which this is a companion.
    */
  private[versions] val ordering: Ordering[V]

  /**
    * Returns the extended version companion object associated with this
    * version type.
    *
    * @return the extended version companion object associated with this
    *         version type
    */
  private[versions] def extendedVersionCompanion: ExtendedVersionCompanion[V, EV]

  /**
    * Returns a partial function which may create a version from an array of values.
    *
    * @return a partial function which may create a version from an array of values
    */
  protected def versionFromArray: PartialFunction[Array[Int], V]

  /**
    * Parses a string into a version.
    *
    * @param v the string to parse
    * @throws VersionFormatException if the given string is not a valid version
    * @return the version represented by the string
    */
  @throws[VersionFormatException]
  def parseVersion(v: String): V = {
    try {
      val a = v split '.' map Integer.parseInt
      versionFromArray.applyOrElse(a, (_: Array[Int]) => throw new VersionFormatException(v))
    } catch {
      case e@(_: IllegalArgumentException | _: NumberFormatException) => throw new VersionFormatException(v, e)
    }
  }
}
