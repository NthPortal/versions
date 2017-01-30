package com.nthportal.versions

/**
  * A definition of an extension type for a version.
  *
  * If this type has a default extension value, it means that a version string
  * without an extension (such as `2.2.7`) is considered to have that extension.
  *
  * @param default  the default extension value, if one exists
  * @param ordering an ordering for extensions of this type
  * @tparam E the type of the extension
  */
case class ExtensionDef[E](default: Option[E], ordering: Ordering[E]) extends Ordering[E] {
  /**
    * Converts an extension of this type to a string.
    *
    * @param ext an extension of this type
    * @return the string representation of the extension
    */
  def extToString(ext: E): String = default match {
    case Some(extension) => if (ext == extension) "" else nonDefaultExtensionToString(ext)
    case None => nonDefaultExtensionToString(ext)
  }

  @inline
  private def nonDefaultExtensionToString(extension: E): String = s"-$extension"

  override def compare(x: E, y: E): Int = ordering.compare(x, y)
}

object ExtensionDef {
  /**
    * Defines an extension type from an [[Ordered]] type, with no default extension value.
    *
    * @tparam E the type of the extension
    * @return an extension definition for the given type
    */
  def fromOrdered[E <: Ordered[E]]: ExtensionDef[E] = apply(None, _ compare _)

  /**
    * Defines an extension type from an [[Ordered]] type, with the given default extension value.
    *
    * @param default the default extension value
    * @tparam E the type of the extension
    * @return an extension definition for the given type and default value
    */
  def fromOrdered[E <: Ordered[E]](default: E): ExtensionDef[E] = apply(Some(default), _ compare _)

  /**
    * Defines an extension type from a [[Comparable]] type, with no default extension value.
    *
    * @tparam E the type of the extension
    * @return an extension definition for the given type
    */
  def fromComparable[E <: Comparable[E]]: ExtensionDef[E] = apply(None, _ compareTo _)

  /**
    * Defines an extension type from a [[Comparable]] type, with the given default extension value.
    *
    * @param default the default extension value
    * @tparam E the type of the extension
    * @return an extension definition for the given type and default value
    */
  def fromComparable[E <: Comparable[E]](default: E): ExtensionDef[E] = apply(Some(default), _ compareTo _)
}
