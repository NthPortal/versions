package com.nthportal.versions

import com.nthportal.convert.Convert

import scala.language.higherKinds

/**
  * Base trait for a version
  *
  * @tparam V  the type of the version
  * @tparam EV the type of the extended version associated with the version
  */
trait VersionBase[V <: VersionBase[V, EV], EV[E] <: ExtendedVersionBase[V, E, EV]]
  extends Ordered[V]
          with Dash[V, EV] {
  /**
    * Returns the [[VersionCompanion companion object]] for this version.
    *
    * @return the companion object for this version
    */
  protected def companion: VersionCompanion[V, EV]

  /**
    * Returns the [[ExtendedVersionCompanion extended version companion object]]
    * associated with this version.
    *
    * @return the extended version companion object associated with this version
    */
  protected[versions] def extendedCompanion: ExtendedVersionCompanion[V, EV]

  /**
    * Returns a [[Seq]] representation of this version.
    *
    * @return a Seq representation of this version
    */
  def toSeq: Seq[Int]

  /**
    * Converts this version to another type.
    *
    * @param companion a [[VersionCompanion companion]] of the type of version
    *                  to which this should be converted
    * @return this version converted to the other type, if it can be
    *         represented by the other type
    * @throws IllegalArgumentException if this version cannot be converted
    *                                  to the other type
    */
  @throws[IllegalArgumentException]("if this version cannot be converted to the other type")
  def to[V2 <: VersionBase[V2, EV2], EV2[E] <: ExtendedVersionBase[V2, E, EV2]]
  (companion: VersionCompanion[V2, EV2])(implicit c: Convert): c.Result[V2] = {
    import c._
    conversion {
      if (companion eq this.companion) this.asInstanceOf[V2]
      else {
        companion.versionFromSeq.applyOrElse(toSeq,
          (_: Seq[Int]) => fail(new IllegalArgumentException(s"$this cannot be converted to $companion")))
      }
    }
  }

  override def dash[E](extension: E)(implicit ed: ExtensionDef[E]) = {
    extendedCompanion(this.asInstanceOf[V], extension, ed)
  }

  override def compare(that: V) = companion.ordering.compare(this.asInstanceOf[V], that)
}
