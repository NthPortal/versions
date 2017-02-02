package com.nthportal.versions.semver

import com.nthportal.versions.semver._build_metadata._

/**
  * Metadata represented by a string.
  *
  * @param value the metadata as a string
  */
case class StringMetadata(value: String) extends BuildMetadata

object StringMetadata {
  /**
    * A [[BuildMetadata.Parser]] for [[StringMetadata]].
    *
    * @return a parser for StringMetadata
    */
  implicit def buildMetadataParser: BuildMetadata.Parser[StringMetadata] = _parser(apply)
}
