package com.nthportal.versions
package variable

final case class Version private(private[variable] val parts: IndexedSeq[Int])
  extends VersionBase[Version, ExtendedVersion] {
  require(parts forall { _ >= 0 }, "Version numbers must be non-negative")

  def size: Int = parts.size

  override protected def companion = Version

  override protected def extendedCompanion = ExtendedVersion

  override def toSeq: Seq[Int] = parts

  override def toString = parts mkString "."
}

object Version extends Versions.OfSize(1 to 16) {
  override private[versions] val ordering: Ordering[Version] = {
    import Ordering.Implicits._

    // It's so nice that this just works
    Ordering by { _.parts }
  }
}
