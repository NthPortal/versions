package com.nthportal.versions
package variable

import com.nthportal.convert.Convert

/**
  * A [[VersionCompanion companion]] for [[Version]]s.
  */
abstract class Companion private[variable]() extends VersionCompanion[Version, ExtendedVersion] {
  override private[versions] val ordering = Companion.ordering

  /** Checks whether a sequence is an allowed size */
  protected def allowedSize(seq: Seq[Int]): Boolean

  /** An error message for sizes which are not allowed */
  protected def invalidSizeMessage: String

  /** Throws an exception if a sequence is not an allowed size */
  @inline
  @throws[IllegalArgumentException]
  private[variable] def checkSize(seq: Seq[Int]): Unit = require(allowedSize(seq), invalidSizeMessage)

  /**
    * Returns a [[Version]] with the specified values.
    *
    * @param values the values of the version
    * @return a Version with the specified values
    * @throws scala.IllegalArgumentException if the number of values does not
    *                                        conform to the allowed sizes
    */
  @throws[IllegalArgumentException]("if the number of values does not conform to the allowed sizes")
  def apply(values: Int*): Version = {
    val vector = values.toVector
    checkSize(vector)
    Version(vector)
  }

  override protected[versions] def versionFromSeq = { case seq if allowedSize(seq) => Version(seq.toVector) }

  /**
    * Extracts values from a version.
    *
    * @param version the version from which to extract values
    * @return the values of the version
    */
  def unapplySeq(version: Version): Option[Seq[Int]] = Some(version.values) filter allowedSize

  /**
    * Extracts a version from a string.
    *
    * @param version the string from which to extract a version
    * @return an [[scala.Option Option]] containing the values of the version;
    *         [[scala.None None]] if the string did not represent a valid version
    *         or was of invalid size
    */
  def unapplySeq(version: String): Option[Seq[Int]] = parseVersion(version)(Convert.Any) map { _.values }
}

private object Companion {
  private val ordering: Ordering[Version] = {
    import Ordering.Implicits._

    Ordering by { _.values } // It's so nice that this just works
  }
}
