package com.nthportal.versions

/**
  * An exception which is thrown when attempting to create a version
  * from a string which is not a valid version string.
  *
  * @param version the invalid version string
  * @param cause the root cause of this exception
  */
final class VersionFormatException(version: String, cause: Throwable)
  extends IllegalArgumentException(s"Invalid version string: $version", cause) {
  /**
    * Creates an exception with the given invalid version string.
    *
    * @param version the invalid version string string
    * @return an exception from the given invalid version string
    */
  def this(version: String) = this(version, null)
}
