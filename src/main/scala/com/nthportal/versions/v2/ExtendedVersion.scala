package com.nthportal.versions
package v2

case class ExtendedVersion[E](version: Version, extension: E, protected val extensionDef: ExtensionDef[E])
  extends Ordered[ExtendedVersion[E]] {
  override def compare(that: ExtendedVersion[E]): Int = extensionDef.ordering2.compare(this, that)
}
