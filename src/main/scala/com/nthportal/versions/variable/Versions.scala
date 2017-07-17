package com.nthportal.versions
package variable

/**
  * Utility object for creating [[VersionCompanion companions]]
  * for [[Version]]s which only allow creating versions whose sizes
  * are within a specified range.
  */
object Versions {
  private[variable] val ordering: Ordering[Version] = {
    import Ordering.Implicits._

    // It's so nice that this just works
    Ordering by { _.values }
  }

  /**
    * A [[VersionCompanion companion]] for [[Version]]s which only allows
    * creating versions whose [[Version.size sizes]] are within a specified range.
    *
    * @param range the range in which versions' sizes must be
    */
  case class OfSize private[variable](range: Range) extends VersionCompanion[Version, ExtendedVersion] {
    require(range.min > 0, "versions must have a positive number of parts")

    override private[versions] val ordering = Versions.ordering

    override protected[versions] def versionFromSeq = {
      case seq if range contains seq.length => new Version(seq.toVector)
    }

    /**
      * Returns a [[ExtendedVersionCompanion companion]] for an [[ExtendedVersion]],
      * with the same size range as this.
      *
      * @return a companion for an ExtendedVersion with the same size range as this
      */
    def extended: ExtendedVersionCompanion[Version, ExtendedVersion] = ExtendedVersions.From(this)

    /**
      * Returns a [[Version]] with the specified values.
      *
      * @param values the values of the version
      * @return a Version with the specified values
      * @throws IllegalArgumentException if the number of values does not
      *                                  conform to the allowed sizes
      */
    @throws[IllegalArgumentException]
    def apply(values: Int*): Version = {
      val vector = values.toVector
      require(range contains vector.size, s"version size not in range: $range")
      new Version(vector)
    }

    /**
      * Extracts a version from a string.
      *
      * @param version the string from which to extract a version
      * @return an [[Option]] containing the values of the version;
      *         [[None]] if the string did not represent a valid version
      *         or was of invalid size
      */
    def unapplySeq(version: String): Option[Seq[Int]] = parseAsOption(version) map { _.values }
  }

  /**
    * Returns a [[VersionCompanion companion]] for [[Version]]s which only allows
    * creating versions whose [[Version.size sizes]] are within a specified range.
    *
    * @param range the range in which versions' sizes must be
    * @return a companion for Versions which only allows creating
    *         versions whose sizes are within a specified range
    */
  def ofSize(range: Range): OfSize = OfSize(range)
}
