package com.nthportal.versions.v2

/**
 * Extractor for [[ExtendedVersion]]s.
 */
trait Extractor {

  /**
   * Extracts the [[Version version]] and extension from an
   * [[ExtendedVersion]].
   *
   * @param ev the extended version from which to extract values
   * @tparam E the type of the extension
   * @return an [[Option]] containing the version and extension
   */
  def unapply[E](ev: ExtendedVersion[E]): Some[(Version, E)] = Some(ev.version -> ev.extension)
}
