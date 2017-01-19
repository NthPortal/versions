package com.nthportal.versions

private[versions] trait VersionCompanion[V <: VersionBase[V]] {
  @throws[VersionFormatException]
  def parseVersion(v: String): V
}
