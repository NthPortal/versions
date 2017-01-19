package com.nthportal.versions

case class ExtensionDef[E](default: E, ordering: Ordering[E]) {
  private implicit def eOrd = ordering

  lazy val ordering2: Ordering[v2.ExtendedVersion[E]] = Ordering by (ev => (ev.version, ev.extension))
  lazy val ordering3: Ordering[v3.ExtendedVersion[E]] = Ordering by (ev => (ev.version, ev.extension))
}
