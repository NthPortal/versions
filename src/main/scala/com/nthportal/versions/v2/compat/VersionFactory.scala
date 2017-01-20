package com.nthportal.versions
package v2
package compat

case class VersionFactory[E](extensionDef: ExtensionDef[E], parser: ExtensionParser[E]) {
  def parseVersion(v: String): ExtendedVersion[E] = ExtendedVersion.parseVersion(v)(extensionDef, parser)
}
