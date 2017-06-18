package com.nthportal.versions.v2

trait Extractor {
  def unapply[E](ev: ExtendedVersion[E]): Some[(Version, E)] = Some(ev.version -> ev.extension)
}
