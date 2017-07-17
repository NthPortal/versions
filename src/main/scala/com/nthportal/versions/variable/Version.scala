package com.nthportal.versions
package variable

import scala.collection.immutable

/**
  * A version with an arbitrary number of '.'-separated values.
  *
  * @param values the values of the version
  */
final case class Version private(values: immutable.IndexedSeq[Int]) extends VersionBase[Version, ExtendedVersion] {
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
}

object Version extends Versions.OfSize(1 to 16) {
  override private[versions] val ordering: Ordering[Version] = {
    import Ordering.Implicits._

    // It's so nice that this just works
    Ordering by { _.values }
  }
}
