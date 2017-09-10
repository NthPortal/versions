package com.nthportal.versions

import com.nthportal.convert.Convert

import scala.language.higherKinds

/**
  * A companion object for a version.
  *
  * @tparam V  the type of the version
  * @tparam EV the type of the extended version associated with the version
  */
trait VersionCompanion[V <: VersionBase[V, EV], EV[E] <: ExtendedVersionBase[V, E, EV]] {
  /**
    * An ordering for the version for which this is a companion.
    */
  private[versions] val ordering: Ordering[V]

  /**
    * Returns a partial function which may create a version from a sequence of values.
    *
    * @return a partial function which may create a version from a sequence of values
    */
  protected[versions] def versionFromSeq: PartialFunction[Seq[Int], V]

  /**
    * Parses a string into a version.
    *
    * @param version the string to parse
    * @param c the `Convert` to use
    * @throws VersionFormatException if the given string is not a valid version
    *                                (when `c` is `Convert.Valid`)
    * @return the version represented by the string
    */
  @throws[VersionFormatException]("if the given string is not a valid version")
  def parseVersion(version: String)(implicit c: Convert): c.Result[V] = {
    import c._
    conversion {
      try {
        require(!version.endsWith("."), "version cannot end with a '.'")
        val seq = version.split('.')
          .map(s => unwrap(parseInt(s)))
          .toSeq
        versionFromSeq.applyOrElse(seq, (_: Seq[Int]) => fail(new VersionFormatException(version)))
      } catch {
        case e: IllegalArgumentException => fail(new VersionFormatException(version, e))
      }
    }
  }
}
