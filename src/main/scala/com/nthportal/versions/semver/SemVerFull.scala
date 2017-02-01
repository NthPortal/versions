package com.nthportal.versions
package semver

/**
  * A [[http://semver.org/spec/2.0.0.html SemVer 2.0.0]] version possibly
  * containing build metadata.
  *
  * @param extendedVersion the [[v3.ExtendedVersion version with an extension]]
  * @param buildMetadata the build metadata, if any exists
  * @tparam E the type of the version extension
  */
case class SemVerFull[E](extendedVersion: v3.ExtendedVersion[E], buildMetadata: Option[String])
  extends Ordered[SemVerFull[E]] {
  override def compare(that: SemVerFull[E]): Int = this.extendedVersion compare that.extendedVersion

  override def toString = extendedVersion + {
    buildMetadata match {
      case Some(data) => s"+$data"
      case None => ""
    }
  }
}
