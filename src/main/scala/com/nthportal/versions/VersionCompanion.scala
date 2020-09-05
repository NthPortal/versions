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
   * Returns a partial function which may create a version from an array of values.
   *
   * @return a partial function which may create a version from an array of values
   */
  protected def versionFromArray: PartialFunction[Array[Int], V]

  /**
   * Parses a string into a version.
   *
   * @param version the string to parse
   * @throws VersionFormatException if the given string is not a valid version
   * @return the version represented by the string
   */
  @throws[VersionFormatException]
  def parseVersion(@deprecatedName('v, since = "1.3.0") version: String): V = {
    try {
      require(!version.endsWith("."), "version cannot end with a '.'")
      val a = version split '.' map Integer.parseInt
      versionFromArray.applyOrElse(a, (_: Array[Int]) => throw new VersionFormatException(version))
    } catch {
      case e: IllegalArgumentException => throw new VersionFormatException(version, e)
    }
  }

  /**
   * Parses a string into a version.
   *
   * @param version the string to parse
   * @return an [[Option]] containing the version represented by the string;
   *         [[None]] if the string did not represent a valid version
   */
  def parseAsOption(version: String): Option[V] = formatCheckToOption { parseVersion(version) }
}
