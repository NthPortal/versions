package com.nthportal.versions

/**
  * A package containing utilities related to
  * [[http://semver.org/spec/2.0.0.html SemVer 2.0.0]] versions.
  *
  * Note: This package is not associated with the SemVer organization in
  * any way; it is merely designed to comply with the SemVer specification.
  */
package object semver {
  private val sectionRegex = """[0-9A-Za-z-]+(\.[0-9A-Za-z-]+)*""".r

  /**
    * Parses a version string into a SemVer version. Strips build metadata
    * from the version string if present.
    *
    * @param version the version string to parse
    * @param ed      the [[ExtensionDef extension definition]] with which to parse
    * @param ep      a [[ExtensionParser parser]] for extensions
    * @tparam E the type of the extension
    * @throws VersionFormatException if the given string is not a valid SemVer version
    * @return the SemVer version represented by the specified version
    */
  @throws[VersionFormatException]
  def parseSemVerVersion[E](version: String)
                           (implicit ep: ExtensionParser[E],
                            ed: ExtensionDef[E]): v3.ExtendedVersion[E] = {
    import BuildMetadata.stringMetadataParser

    parseSemVerWithBuildMetadata(version).extendedVersion
  }

  /**
    * Parses a version string into a SemVer version. Keeps and parses build metadata
    * if present.
    *
    * @param version the version string to parse
    * @param ed      the [[ExtensionDef extension definition]] with which to parse
    * @param ep      a [[ExtensionParser parser]] for extensions
    * @tparam E the type of the extension
    * @throws VersionFormatException if the given string is not a valid SemVer version
    * @return the SemVer version represented by the specified version
    */
  @throws[VersionFormatException]
  def parseSemVerWithBuildMetadata[E, M](version: String)
                                        (implicit ep: ExtensionParser[E],
                                         ed: ExtensionDef[E],
                                         mp: BuildMetadata.Parser[M]): SemVerFull[E, M] = {
    try {
      version split '+' match {
        case Array(ver, meta) =>
          validateSemVerSection(meta, "build metadata")
          SemVerFull(parseExtendedVersion(ver), Some(mp.parse(meta)))
        case Array(ver) =>
          require(!version.endsWith("+"), "build metadata cannot be empty")
          SemVerFull(parseExtendedVersion(ver), None)
        case _ => throw new IllegalArgumentException("SemVer versions may only have a single build metadata section")
      }
    } catch {
      case e: IllegalArgumentException => throw new VersionFormatException(version, e)
    }
  }

  @inline
  @throws[IllegalArgumentException]
  private def validateSemVerSection(contents: String, sectionName: String): Unit = {
    require(sectionRegex.pattern.matcher(contents).matches(), s"Invalid $sectionName: " + contents)
  }

  @inline
  @throws[IllegalArgumentException]
  private def parseExtendedVersion[E](version: String)
                                     (implicit ep: ExtensionParser[E],
                                      ed: ExtensionDef[E]): v3.ExtendedVersion[E] = {
    v3.ExtendedVersion.parseVersion(version)(ed, _extensionParser(extension => {
      validateSemVerSection(extension, "extension")
      ep.parse(extension)
    }))
  }

  implicit final class RichVersion(private val ver: v3.Version) extends AnyVal {
    /**
      * Returns a [[v3.Version version]] with the major version number
      * incremented by `1`, as described in
      * [[http://semver.org/spec/v2.0.0.html#spec-item-8 the specification]].
      *
      * @return a version with the major version number incremented by `1`
      */
    def bumpMajor: v3.Version = v3.Version(ver.major + 1, 0, 0)

    /**
      * Returns a [[v3.Version version]] with the minor version number
      * incremented by `1`, as described in
      * [[http://semver.org/spec/v2.0.0.html#spec-item-7 the specification]].
      *
      * @return a version with the minor version number incremented by `1`
      */
    def bumpMinor: v3.Version = ver.copy(minor = ver.minor + 1, patch = 0)

    /**
      * Returns a [[v3.Version version]] with the patch version number
      * incremented by `1`, as described in
      * [[http://semver.org/spec/v2.0.0.html#spec-item-6 the specification]].
      *
      * @return a version with the patch version number incremented by `1`
      */
    def bumpPatch: v3.Version = ver.copy(patch = ver.patch + 1)
  }

  implicit final class RichExtendedVersion[E](private val ver: v3.ExtendedVersion[E]) extends AnyVal {
    /**
      * Returns a [[v3.ExtendedVersion version]] with the major version number
      * incremented by `1`, as described in
      * [[http://semver.org/spec/v2.0.0.html#spec-item-8 the specification]].
      *
      * @return a version with the major version number incremented by `1`
      */
    def bumpMajor: v3.ExtendedVersion[E] = ver.copy(version = ver.version.bumpMajor)

    /**
      * Returns a [[v3.ExtendedVersion version]] with the minor version number
      * incremented by `1`, as described in
      * [[http://semver.org/spec/v2.0.0.html#spec-item-7 the specification]].
      *
      * @return a version with the minor version number incremented by `1`
      */
    def bumpMinor: v3.ExtendedVersion[E] = ver.copy(version = ver.version.bumpMinor)

    /**
      * Returns a [[v3.ExtendedVersion version]] with the patch version number
      * incremented by `1`, as described in
      * [[http://semver.org/spec/v2.0.0.html#spec-item-6 the specification]].
      *
      * @return a version with the patch version number incremented by `1`
      */
    def bumpPatch: v3.ExtendedVersion[E] = ver.copy(version = ver.version.bumpPatch)

    /**
      * Creates a [[SemVerFull SemVer version]] with empty build metadata
      * from this extended version.
      *
      * @tparam M the type of the build metadata
      * @return a SemVer version with empty build metadata
      */
    def withNoMetadata[M]: SemVerFull[E, M] = SemVerFull(ver, None)

    /**
      * Creates a [[SemVerFull SemVer version]] with the specified build
      * metadata from this extended version.
      *
      * @param metadata the build metadata
      * @tparam M the type of the build metadata
      * @return a SemVer version with the specified build metadata
      */
    def withBuildMetadata[M](metadata: M): SemVerFull[E, M] = SemVerFull(ver, Some(metadata))

    /**
      * @see [[withBuildMetadata]]
      */
    def +[M](buildMetadata: M): SemVerFull[E, M] = withBuildMetadata(buildMetadata)
  }

}
