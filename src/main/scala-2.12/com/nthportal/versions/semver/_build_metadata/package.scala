package com.nthportal.versions.semver

package object _build_metadata {
  @inline
  private[semver] def _parser[M](f: String => M): BuildMetadata.Parser[M] = f(_)
}
