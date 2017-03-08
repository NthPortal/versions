package com.nthportal.versions
package semver

import semver._

/**
  * A [[http://semver.org/spec/2.0.0.html SemVer 2.0.0]] version possibly
  * containing build metadata.
  *
  * @param extendedVersion the [[v3.ExtendedVersion version with an extension]]
  * @param buildMetadata   the build metadata, if any exists
  * @tparam E the type of the version extension
  * @tparam M the type of the build metadata
  */
case class SemVerFull[E, M](extendedVersion: v3.ExtendedVersion[E], buildMetadata: Option[M])
  extends Ordered[SemVerFull[E, _]] {
  /**
    * Returns a [[SemVerFull version]] with the major version number
    * incremented by `1`, as described in
    * [[http://semver.org/spec/v2.0.0.html#spec-item-8 the specification]].
    *
    * @return a version with the major version number incremented by `1`
    */
  def bumpMajor: SemVerFull[E, M] = copy(extendedVersion = extendedVersion.bumpMajor)

  /**
    * Returns a [[SemVerFull version]] with the minor version number
    * incremented by `1`, as described in
    * [[http://semver.org/spec/v2.0.0.html#spec-item-7 the specification]].
    *
    * @return a version with the minor version number incremented by `1`
    */
  def bumpMinor: SemVerFull[E, M] = copy(extendedVersion = extendedVersion.bumpMinor)

  /**
    * Returns a [[SemVerFull version]] with the patch version number
    * incremented by `1`, as described in
    * [[http://semver.org/spec/v2.0.0.html#spec-item-6 the specification]].
    *
    * @return a version with the patch version number incremented by `1`
    */
  def bumpPatch: SemVerFull[E, M] = copy(extendedVersion = extendedVersion.bumpPatch)

  override def compare(that: SemVerFull[E, _]): Int = this.extendedVersion compare that.extendedVersion

  override def toString = extendedVersion.toString + {
    buildMetadata match {
      case Some(data) => s"+$data"
      case None => ""
    }
  }
}
