package com.nthportal.versions
package variable

object Versions {

  case class OfSize private[variable](range: Range) extends VersionCompanion[Version, ExtendedVersion] {
    require(range.min > 0, "versions must have a positive number of parts")

    override private[versions] val ordering = Version.ordering

    override protected[versions] def versionFromSeq = { case seq if range contains seq.length => Version(seq.toVector) }

    def extended: ExtendedVersionCompanion[Version, ExtendedVersion] = ExtendedVersions.From(this)

    def apply(parts: Int*): Version = {
      val vector = parts.toVector
      require(vector contains parts.size, s"version size not in range: $range")
      Version(vector)
    }

    def unapplySeq(version: String): Option[Seq[Int]] = parseAsOption(version) map { _.parts }
  }

  def ofSize(range: Range): OfSize = OfSize(range)
}
