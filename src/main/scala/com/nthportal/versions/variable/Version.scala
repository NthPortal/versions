package com.nthportal.versions
package variable

import scala.collection.immutable

/**
  * A version with an arbitrary number of '.'-separated values.
  *
  * @param values the values of the version
  */
final class Version private[variable](val values: immutable.IndexedSeq[Int])
  extends VersionBase[Version, ExtendedVersion] {
  require(values forall { _ >= 0 }, "Version numbers must be non-negative")

  /**
    * Returns the size of the version (the number of
    * '.'-separated values).
    *
    * @return returns the size of the version
    */
  def size: Int = values.size

  override protected def companion = Version

  override protected def extendedCompanion = ExtendedVersion

  override def toSeq: Seq[Int] = values

  override def toString = values mkString "."

  override def equals(obj: Any) = obj match {
    case that: Version => this.values == that.values
    case _ => false
  }

  override def hashCode() = values.hashCode()
}

object Version extends Versions.OfSize(1 to 16)
