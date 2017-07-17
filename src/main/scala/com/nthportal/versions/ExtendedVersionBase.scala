package com.nthportal
package versions

import scala.language.higherKinds

/**
  * Base trait for an extended version.
  *
  * @tparam V  the type of the version component of this extended version
  * @tparam E  the type of the extension component of this extended version
  * @tparam EV the type of this extended version
  */
trait ExtendedVersionBase[V <: VersionBase[V, EV], E, EV[X] <: ExtendedVersionBase[V, X, EV]]
  extends Ordered[EV[E]] {
  /**
    * The version component of this extended version.
    */
  val version: V
  /**
    * The extension component of this extended version.
    */
  val extension: E
  /**
    * The [[ExtensionDef extension definition]] for this version's extension.
    */
  protected val extensionDef: ExtensionDef[E]

  /**
    * Converts this extended version to another type.
    *
    * @param companion a [[ExtendedVersionCompanion companion]] of the type
    *                  of version to which this should be converted
    * @return an [[Option]] containing this version converted to
    *         the other type, if it can be represented by the other type
    */
  def to[V2 <: VersionBase[V2, EV2], EV2[X] <: ExtendedVersionBase[V2, X, EV2]]
  (companion: ExtendedVersionCompanion[V2, EV2]): Option[EV2[E]] = {
    companion.baseCompanion.versionFromSeq.lift(version.toSeq) map { companion(_, extension, extensionDef) }
  }

  /**
    * Compares two extended versions. Adheres to the general contract
    * of `compare` as defined in [[scala.math.Ordered.compare]].
    *
    * `that` MUST have the same [[Ordering]] in its extension definition as `this`.
    *
    * @param that the extended version to compare to this
    * @throws IllegalArgumentException if `that` does not have the same
    *                                  extension definition as this
    * @return the result of comparing `this` with `that`
    */
  @throws[IllegalArgumentException]
  override def compare(that: EV[E]): Int = {
    require(extensionDef.ordering == that.extensionDef.ordering,
      "cannot compare extended versions with different extension orderings")
    implicit val eOrd = extensionDef.ordering
    (this.version compare that.version) thenCompare (this.extension, that.extension)
  }

  override def toString = s"$version${extensionDef.extToString(extension)}"
}
