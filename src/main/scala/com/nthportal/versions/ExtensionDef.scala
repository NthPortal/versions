package com.nthportal.versions

case class ExtensionDef[E](default: Option[E], ordering: Ordering[E]) {
  private implicit def eOrd = ordering

  val ordering2: Ordering[v2.ExtendedVersion[E]] = Ordering by (ev => (ev.version, ev.extension))
  val ordering3: Ordering[v3.ExtendedVersion[E]] = Ordering by (ev => (ev.version, ev.extension))

  def extToString(ext: E): String = default match {
    case Some(extension) => if (ext == extension) "" else nonDefaultExtensionToString(ext)
    case None => nonDefaultExtensionToString(ext)
  }

  @inline
  private def nonDefaultExtensionToString(extension: E): String = s"-$extension"
}
