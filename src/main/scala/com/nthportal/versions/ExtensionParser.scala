package com.nthportal.versions

trait ExtensionParser[E] {
  @throws[IllegalArgumentException]
  def parse(extension: String): E
}
