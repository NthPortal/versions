package com.nthportal
package versions

import com.nthportal.convert.Convert

/**
  * A package containing utilities related to
  * [[http://semver.org/spec/2.0.0.html SemVer 2.0.0]] versions.
  *
  * Note: This package is not associated with the SemVer organization in
  * any way; it is merely designed to comply with the SemVer specification.
  */
package object semver {
  private val sectionRegex = """[0-9A-Za-z-]+(\.[0-9A-Za-z-]+)*""".r
  private val digitRegex = """\d+""".r

  /**
    * Parses a version string into a [[SemanticVersion SemVer version]].
    *
    * @param version the version string to parse
    * @param c       the `Convert` to use
    * @param ed      the [[ExtensionDef extension definition]] with which to parse
    * @param ep      a [[ExtensionParser parser]] for extensions
    * @tparam E the type of the extension
    * @throws VersionFormatException if the given string is not a valid SemVer version
    *                                (when `c` is `Convert.Valid`)
    * @return the SemVer version represented by the specified version
    */
  @throws[VersionFormatException]("if the given string is not a valid SemVer version")
  def parseSemVer[E, M](version: String)
                       (implicit c: Convert,
                        ed: ExtensionDef[E],
                        ep: ExtensionParser[E],
                        mp: BuildMetadata.Parser[M]): c.Result[SemanticVersion[E, M]] = {
    import c._
    conversion {
      try {
        version split '+' match {
          case Array(ver, meta) =>
            validateSemVerSection(meta, "build metadata")
            SemanticVersion(parseExtendedVersion(ver)(c), Some(mp.parse(meta)))
          case Array(ver) =>
            require(!version.endsWith("+"), "build metadata cannot be empty")
            SemanticVersion(parseExtendedVersion(ver)(c), None)
          case _ => fail(new IllegalArgumentException("SemVer versions may only have a single build metadata section"))
        }
      } catch {
        case e: IllegalArgumentException => fail(new VersionFormatException(version, e))
      }
    }
  }

  /**
    * Parses a SemVer version string into a [[v3.Version version]].
    * The version string must not contain build metadata.
    *
    * @param version the version string to parse
    * @param c       the `Convert` to use
    * @param ed      the [[ExtensionDef extension definition]] with which to parse
    * @param ep      a [[ExtensionParser parser]] for extensions
    * @tparam E the type of the extension
    * @throws VersionFormatException if the given string is not a valid SemVer version
    *                                or contains build metadata (when `c` is `Convert.Valid`)
    * @return the SemVer version represented by the specified version string
    */
  @throws[VersionFormatException]("if the given string is not a valid SemVer version or contains build metadata")
  def parseSemVerWithoutMetadata[E](version: String)
                                   (implicit c: Convert,
                                    ed: ExtensionDef[E],
                                    ep: ExtensionParser[E]): c.Result[v3.ExtendedVersion[E]] = {
    import c._
    conversion {
      try {
        val semVer = unwrap(parseSemVer(version))
        require(semVer.buildMetadata.isEmpty, "version contains build metadata")
        semVer.extendedVersion
      } catch {
        case e: IllegalArgumentException => fail(new VersionFormatException(version, e))
      }
    }
  }

  /**
    * Extractor for [[SemanticVersion SemVer versions]].
    */
  object SemVer {
    /**
      * Extracts a [[SemanticVersion SemVer version]] (including build metadata)
      * from a version string.
      *
      * @param version the string from which to extract a SemVer version
      * @param ed      the [[ExtensionDef extension definition]] with which to parse
      * @param ep      a [[ExtensionParser parser]] for extensions
      * @tparam E the type of the extension
      * @tparam M the type of the build metadata
      * @return an [[scala.Option Option]] containing the version, extension, and build metadata
      *         represented by the version string; [[scala.None None]] if the string did not
      *         represent a valid SemVer version
      */
    def unapply[E, M](version: String)
                     (implicit ed: ExtensionDef[E],
                      ep: ExtensionParser[E],
                      mp: BuildMetadata.Parser[M]): Option[(v3.Version, E, Option[M])] = {
      import Convert.Any.Implicit.ref
      parseSemVer(version) map { semVer => (semVer.version, semVer.extension, semVer.buildMetadata) }
    }
  }

  /**
    * Extractor for [[SemanticVersion SemVer versions]].
    *
    * Example usage:
    *
    * {{{
    * val sv = Version(1, 0, 0) -- "beta" + 12654
    *
    * sv match {
    *   case ev + meta => println(s"metadata: \$meta")
    * }
    * }}}
    */
  object + {
    /**
      * Extracts the [[v3.ExtendedVersion extended version]] and build metadata
      * from a [[SemanticVersion SemVer version]].
      *
      * @param sv the [[SemanticVersion SemVer version]] from which to extract
      *           values
      * @tparam E the type of the extension
      * @tparam M the type of the build metadata
      * @return an [[scala.Option Option]] containing the extended version and build metadata;
      *         [[scala.None None]] if the [[SemanticVersion SemVer version]] does not
      *         contain build metadata
      */
    def unapply[E, M](sv: SemanticVersion[E, M]): Option[(v3.ExtendedVersion[E], M)] = {
      sv.buildMetadata match {
        case Some(meta) => Some(sv.extendedVersion -> meta)
        case None => None
      }
    }
  }

  /**
    * Extractor for [[SemanticVersion SemVer versions]].
    *
    * Example usage:
    *
    * {{{
    * val sv = Version(1, 0, 0) -- "beta" + 12654
    *
    * sv match {
    *   case ev +? metadata =>
    *     metadata match {
    *       case Some(meta) => println(s"metadata: \$meta")
    *       case None => println("no metadata")
    * }
    * }}}
    */
  object +? {
    /**
      * Extracts the [[v3.ExtendedVersion extended version]] and build metadata
      * (if it exists) from a [[SemanticVersion SemVer version]].
      *
      * @param sv the [[SemanticVersion SemVer version]] from which to extract
      *           values
      * @tparam E the type of the extension
      * @tparam M the type of the build metadata
      * @return an [[scala.Option Option]] containing the extended version
      *         and build metadata; the build metadata is an Option
      */
    def unapply[E, M](sv: SemanticVersion[E, M]): Option[(v3.ExtendedVersion[E], Option[M])] = {
      SemanticVersion.unapply(sv)
    }
  }

  @inline
  @throws[IllegalArgumentException]
  private def validateSemVerSection(contents: String, sectionName: String)(implicit c: Convert): Unit = {
    c.require(sectionRegex.pattern.matcher(contents).matches(), s"Invalid $sectionName: " + contents)
  }

  @inline
  @throws[IllegalArgumentException]
  private def parseExtendedVersion[E](version: String)
                                     (c: Convert)
                                     (implicit ep: ExtensionParser[E],
                                      ed: ExtensionDef[E]): v3.ExtendedVersion[E] = {
    c.unwrap {
      v3.ExtendedVersion.parseVersion(version)(c, ed, new ExtensionParser[E] {
        override def parse(extension: String)(implicit c2: Convert): c2.Result[E] = {
          import c2._
          conversion {
            validateSemVerSection(extension, "extension")
            extension split '.' foreach { part =>
              require(!part.startsWith("0")
                || part.length == 1
                || !digitRegex.pattern.matcher(part).matches(),
                "Numeric identifier has leading zero(es): " + part)
            }
            unwrap(ep.parse(extension))
          }
        }
      })
    }
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
      * Creates a [[SemanticVersion SemVer version]] with empty build metadata
      * from this extended version.
      *
      * @tparam M the type of the build metadata
      * @return a SemVer version with empty build metadata
      */
    def withNoMetadata[M]: SemanticVersion[E, M] = +?(None)

    /**
      * Creates a [[SemanticVersion SemVer version]] with the specified build
      * metadata from this extended version.
      *
      * @param metadata the build metadata
      * @tparam M the type of the build metadata
      * @return a SemVer version with the specified build metadata
      */
    def withBuildMetadata[M](metadata: M): SemanticVersion[E, M] = +?(Some(metadata))

    /**
      * @see [[withBuildMetadata]]
      */
    def +[M](buildMetadata: M): SemanticVersion[E, M] = +?(Some(buildMetadata))

    /**
      * Creates a [[SemanticVersion SemVer version]] with the specified build
      * metadata [[scala.Option Option]] from this extended version.
      *
      * @param buildMetadata an Option containing the build metadata
      * @tparam M the type of the build metadata
      * @return a SemVer version with the specified build metadata
      */
    def +?[M](buildMetadata: Option[M]): SemanticVersion[E, M] = SemanticVersion(ver, buildMetadata)
  }

}
