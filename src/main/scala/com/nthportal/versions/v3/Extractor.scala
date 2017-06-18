package com.nthportal.versions.v3

trait Extractor {
  def unapply[E](ev: ExtendedVersion[E]): Some[(Version, E)] = Some(ev.version -> ev.extension)
}
