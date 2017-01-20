package com.nthportal.versions

case class ExtensionDef[E](default: Option[E], ordering: Ordering[E]) {
  def extToString(ext: E): String = default match {
    case Some(extension) => if (ext == extension) "" else nonDefaultExtensionToString(ext)
    case None => nonDefaultExtensionToString(ext)
  }

  @inline
  private def nonDefaultExtensionToString(extension: E): String = s"-$extension"
}

object ExtensionDef {
  def fromOrdered[E <: Ordered[E]]: ExtensionDef[E] = apply(None, _ compare _)

  def fromOrdered[E <: Ordered[E]](default: E): ExtensionDef[E] = apply(Some(default), _ compare _)

  def fromComparable[E <: Comparable[E]]: ExtensionDef[E] = apply(None, _ compareTo _)

  def fromComparable[E <: Comparable[E]](default: E): ExtensionDef[E] = apply(Some(default), _ compareTo _)
}
