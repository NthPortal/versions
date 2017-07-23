package com.nthportal.versions
package variable

import scala.collection.immutable

/**
  * A version with an arbitrary number of '.'-separated values.
  *
  * @param values the values of the version
  */
final class Version private(val values: IndexedSeq[Int])
  extends VersionBase[Version, ExtendedVersion] {
  require(values forall { _ >= 0 }, "Version numbers must be non-negative")

  /**
    * Returns the value at the specified index.
    *
    * @param idx the index of the value in the version
    * @return the value at the specified index
    * @throws IndexOutOfBoundsException if the specified index
    *                                   is out of bounds
    */
  @throws[IndexOutOfBoundsException]
  def apply(idx: Int): Int = values(idx)

  /**
    * Returns the size of the version (the number of
    * '.'-separated values).
    *
    * @return returns the size of the version
    */
  def size: Int = values.size

  override protected def companion = Version

  override protected[versions] def extendedCompanion = ExtendedVersion

  override def toSeq = values

  override def equals(obj: scala.Any) = obj match {
    case that: Version => this.values == that.values
    case _ => false
  }

  override def hashCode() = values.##

  override def toString = values mkString "."
}

/**
  * A [[Companion]] which allows the creation of [[Version]]s of
  * any non-empty size.
  */
object Version extends Companion {
  override protected def allowedSize(seq: Seq[Int]): Boolean = seq.nonEmpty

  override protected def invalidSizeMessage: String = "version must have a positive number of values"

  /**
    * A [[Companion]] for [[Version]]s which only allows creating
    * versions whose [[Version.size sizes]] are within a specified range.
    *
    * @param range the range in which versions' sizes must be
    */
  final case class OfSize private(range: Range) extends Companion {
    require(range.nonEmpty, "size range cannot be empty")
    require(range.min > 0, "versions must have a positive number of values")

    override protected def allowedSize(seq: Seq[Int]): Boolean = range contains seq.size

    override protected def invalidSizeMessage: String = s"version size not in range: $range"

    /**
      * Returns a [[ExtendedVersionCompanion companion]] for an [[ExtendedVersion]],
      * with the same size range as this.
      *
      * @return a companion for an ExtendedVersion with the same size range as this
      */
    def extended: ExtendedVersionCompanion[Version, ExtendedVersion] = ExtendedVersion.OfSize(this)
  }

  /**
    * Returns a [[Companion]] for [[Version]]s which only allows creating
    * versions whose [[Version.size sizes]] are within a specified range.
    *
    * @param range the range in which versions' sizes must be
    * @return a companion for Versions which only allows creating
    *         versions whose sizes are within a specified range
    */
  def ofSize(range: Range): OfSize = OfSize(range)

  /** Creates a version from an [[immutable.IndexedSeq]] */
  private[variable] def apply(values: immutable.IndexedSeq[Int]): Version = new Version(values)
}
