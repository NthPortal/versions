package com.nthportal.versions

case class ExtensionParseDef[E](extensionDef: ExtensionDef[E], parser: ExtensionParser[E])
