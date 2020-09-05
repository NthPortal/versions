package com.nthportal

package object versions {

  /**
   * Extractor for extended versions.
   *
   * Example usage:
   *
   * {{{
   * val ev = Version(1, 0, 0) -- "beta" // does not have to be a v3.ExtendedVersion
   *
   * ev match {
   *   case ver -- ext =>
   *     println(s"version: $ver")
   *     println(s"extension: $ext")
   * }
   * }}}
   */
  object -- extends v2.Extractor with v3.Extractor with v4.Extractor

  /**
   * Alias for [[--]].
   */
  val dash: --.type = --

  private[versions] def formatCheckToOption[V](thunk: => V): Option[V] = {
    try {
      Some(thunk)
    } catch {
      case _: VersionFormatException => None
    }
  }
}
