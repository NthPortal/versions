package com.nthportal.versions.v4

trait Extractor {
  def unapply[E](ev: ExtendedVersion[E]): Some[(Version, E)] = Some(ev.version -> ev.extension)
}
