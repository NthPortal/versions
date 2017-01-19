package com.nthportal.versions
package v3

case class ExtendedVersion[E](version: Version, extension: E, protected val extensionDef: ExtensionDef[E])
  extends Ordered[ExtendedVersion[E]] {
  override def compare(that: ExtendedVersion[E]): Int = extensionDef.ordering3.compare(this, that)
}
