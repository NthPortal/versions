package com.nthportal.versions
package semver

/**
  * A [[http://semver.org/spec/2.0.0.html SemVer 2.0.0]] version possibly
  * containing build metadata.
  *
  * @param extendedVersion the [[v3.ExtendedVersion version with an extension]]
  * @param buildMetadata   the build metadata, if any exists
  * @tparam E the type of the version extension
  * @tparam M the type of the build metadata
  */
final case class SemanticVersion[E, M](extendedVersion: v3.ExtendedVersion[E], buildMetadata: Option[M])
  extends Ordered[SemanticVersion[E, _]] {
  override def compare(that: SemanticVersion[E, _]): Int = this.extendedVersion compare that.extendedVersion

  override def toString = extendedVersion.toString + {
    buildMetadata match {
      case Some(data) => s"+$data"
      case None => ""
    }
  }
}
