package com.nthportal.versions

private[versions] trait VersionBase[V <: VersionBase[V]] extends Ordered[V]
