package com.nthportal.versions

final class VersionFormatException(version: String, cause: Throwable)
  extends IllegalArgumentException(s"Invalid version string: $version", cause) {
  def this(version: String) = this(version, null)
}
