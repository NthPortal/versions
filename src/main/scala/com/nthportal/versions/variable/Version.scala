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

sealed case class WithSize private[variable](range: Range) extends VersionCompanion[Version, ExtendedVersion] {
  require(range.min > 0, "versions must have a positive number of parts")

  override private[versions] val ordering = Version.ordering

  override protected[versions] def versionFromSeq = { case seq if range contains seq.length => Version(seq.toVector) }

  def apply(parts: Int*): Version = {
    val vector = parts.toVector
    require(vector contains parts.size, s"version size not in range: $range")
    Version(vector)
  }

  def unapplySeq(version: String): Option[Seq[Int]] = parseAsOption(version) map { _.parts }
}

object Version extends WithSize(1 to 16) {
  override private[versions] val ordering: Ordering[Version] = {
    import Ordering.Implicits._

    // It's so nice that this just works
    Ordering by { _.parts }
  }

  def withSize(range: Range): WithSize = WithSize(range)
}
