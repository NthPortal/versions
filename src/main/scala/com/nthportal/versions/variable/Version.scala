package com.nthportal.versions
package variable

final case class Version private(private[Version] val parts: IndexedSeq[Int])
  extends VersionBase[Version, ExtendedVersion] {
  require(parts forall { _ >= 0 }, "Version numbers must be non-negative")

  def size: Int = parts.size

  override private[versions] def companion = Version

  override private[versions] def extendedCompanion = ExtendedVersion

  override def toString = parts mkString "."
}

private sealed case class WithSize(range: Range) extends VersionCompanion[Version, ExtendedVersion] {
  require(range.min > 0, "versions must have a positive number of parts")

  override private[versions] val ordering = Version.ordering

  override protected def versionFromArray = { case arr if range contains arr.length => Version(arr.toVector) }

  def apply(parts: Int*): Version = {
    val vector = parts.toVector
    require(vector contains parts.size, s"version size not in range: $range")
    Version(vector)
  }
}

object Version extends WithSize(1 to 16) {
  override private[versions] val ordering: Ordering[Version] = {
    import Ordering.Implicits._

    // It's so nice that this just works
    Ordering by { _.parts }
  }

  def withSize(range: Range): VersionCompanion[Version, ExtendedVersion] = WithSize(range)
}
