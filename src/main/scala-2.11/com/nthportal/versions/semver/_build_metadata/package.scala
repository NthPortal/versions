package com.nthportal.versions.semver

import com.nthportal.versions.semver.BuildMetadata.Parser

package object _build_metadata {
  @inline
  private[semver] def _parser[M](f: String => M): Parser[M] = new Parser[M] {
    override def parse(metadata: String): M = f(metadata)
  }
}
