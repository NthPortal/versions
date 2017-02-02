package com.nthportal.versions.semver

import com.nthportal.versions.semver._build_metadata._

/**
  * Utility object for build metadata parsers. (Metadata for a build may be
  * appended to a [[http://semver.org/spec/v2.0.0.html#spec-item-10 SemVer version]].)
  */
object BuildMetadata {

  /**
    * Returns a [[Parser]] for string build metadata.
    *
    * The parser uses the [[identity]] function.
    *
    * @return a parser for string build metadata
    */
  implicit def stringMetadataParser: Parser[String] = _parser(identity)

  /**
    * Parses a string into build metadata
    *
    * @tparam M the type of the build metadata
    */
  trait Parser[M] {
    /**
      * Parses a string into build metadata.
      *
      * @param metadata the string to parse
      * @throws IllegalArgumentException if the metadata is invalid
      * @return the metadata represented by the string
      */
    @throws[IllegalArgumentException]
    def parse(metadata: String): M
  }

}
