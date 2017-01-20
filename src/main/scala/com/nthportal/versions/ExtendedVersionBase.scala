package com.nthportal.versions

import scala.language.higherKinds

private[versions]
abstract class ExtendedVersionBase[V <: VersionBase[V, EV], E, EV[X] <: ExtendedVersionBase[V, X, EV]]
(orderingFrom: ExtensionDef[E] => Ordering[EV[E]])
  extends Ordered[EV[E]] {
  val version: V
  val extension: E
  protected val extensionDef: ExtensionDef[E]

  override def compare(that: EV[E]): Int = orderingFrom(extensionDef).compare(this.asInstanceOf[EV[E]], that)

  override def toString = s"$version${extensionDef.extToString(extension)}"
}
