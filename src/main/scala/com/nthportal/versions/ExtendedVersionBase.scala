package com.nthportal.versions

private[versions] trait ExtendedVersionBase[V <: VersionBase[V], E] {
  val version: V
  val extension: E
  protected val extensionDef: ExtensionDef[E]
}
