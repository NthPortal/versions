package com.nthportal

package object versions {
  object dash extends v2.Extractor with v3.Extractor with v4.Extractor
  val -- : dash.type = dash

  private[versions] def formatCheckToOption[V](thunk: => V): Option[V] = {
    try {
      Some(thunk)
    } catch {
      case _: VersionFormatException => None
    }
  }
}
