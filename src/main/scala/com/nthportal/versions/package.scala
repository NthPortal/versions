package com.nthportal

package object versions {
  val -- = Dash

  private[versions] def formatCheckToOption[V](thunk: => V): Option[V] = {
    try {
      Some(thunk)
    } catch {
      case _: VersionFormatException => None
    }
  }
}
