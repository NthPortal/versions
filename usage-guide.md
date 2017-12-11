# Table of Contents

- [Versions](#versions)
  - [`v2`](#v2)
  - [`v3`](#v3)
  - [`v4`](#v4)
  - [Version Ordering](#version-ordering)
- [Extended Versions](#extended-versions)
  - [Extension Definitions](#extension-definitions)
  - [Provided Extensions](#provided-extensions)
  - [Creating Extended Versions](#creating-extended-versions)
  - [Parsing Extended Versions](#parsing-extended-versions)
  - [Extractors for Extended Versions](#extractors-for-extended-versions)
  - [Extended Version Ordering](#extended-version-ordering)
- [Aliases](#aliases)
- [SemVer](#semver)
  - [Creating SemVer Versions](#creating-semver-versions)
  - [Parsing SemVer Versions](#parsing-semver-versions)
  - [Convenience Extension Methods](#convenience-extension-methods)
  - [Extractors for SemVer Versions](#extractors-for-semver-versions)
  - [SemVer Version Ordering](#semver-version-ordering)
  

# Versions

This library provides three types of versions in `com.nthportal.versions`: `v2`, `v3` and `v4`, representing versions with two, three and four parts respectively. Each can be constructed in a few different ways, depending on a user's style preferences.

### `v2`

`v2.Version` represents a version with two parts, such as `1.0` or `2.10`. The version `1.3` can be constructed in any of the following ways:

```scala
import com.nthportal.versions.v2._

Version(1, 3)
Version(1)(3)
Version of 1 dot 3
```

Additionally, a version can be parsed from a string, either directly as a `Version`, or as an `Option` (if the string might not represent a valid version):

```scala
import com.nthportal.convert.Convert
import com.nthportal.versions.v2._

implicit val c = Convert.Throwing // or `Convert.AsOption`
Version.parseVersion("1.3")
```

Fields can be extracted from either a `Version` or a string as well:

```scala
import com.nthportal.versions.v2._

Version(1, 3) match {
  case Version(major, minor) => // ...
}
"1.3" match {
  case Version(major, minor) => // ...
}
```

### `v3`

`v3.Version` represents a version with three parts, such as `1.0.0` or `2.10.1`. The version `1.2.5` can be constructed in any of the following ways:

```scala
import com.nthportal.versions.v3._

Version(1, 2, 5)
Version(1)(2)(5)
Version of 1 dot 2 dot 5
```

Additionally, a version can be parsed from a string, either directly as a `Version`, or as an `Option` (if the string might not represent a valid version):

```scala
import com.nthportal.convert.Convert
import com.nthportal.versions.v3._

implicit val c = Convert.Throwing // or `Convert.AsOption`
Version.parseVersion("1.2.5")
```

Fields can be extracted from either a `Version` or a string as well:

```scala
import com.nthportal.versions.v3._

Version(1, 2, 5) match {
  case Version(major, minor, patch) => // ...
}
"1.2.5" match {
  case Version(major, minor, patch) => // ...
}
```

### `v4`

`v4.Version` represents a version with four parts, such as `1.0.0.0` or `2.10.1.8`. The version `1.2.5.4` can be constructed in any of the following ways:

```scala
import com.nthportal.versions.v4._

Version(1, 2, 5, 4)
Version(1)(2)(5)(4)
Version of 1 dot 2 dot 5 dot 4
```

Additionally, a version can be parsed from a string, either directly as a `Version`, or as an `Option` (if the string might not represent a valid version):

```scala
import com.nthportal.convert.Convert
import com.nthportal.versions.v4._

implicit val c = Convert.Throwing // or `Convert.AsOption`
Version.parseVersion("1.2.5.4")
```

Fields can be extracted from either a `Version` or a string as well:

```scala
import com.nthportal.versions.v4._

Version(1, 2, 5, 4) match {
  case Version(a, b, c, d) => // ...
}
"1.2.5.4" match {
  case Version(a, b, c, d) => // ...
}
```

### Version Ordering

Each of `v2`, `v3` and `v4` are `Ordered`, so that they can be compared to versions of the same type. Thus, `v2.Version(2, 0) > v2.Version(1, 3)` is valid, but not `v2.Version(2, 0) > v3.Version(1, 3, 4)`.

# Extended Versions

An extended version is a version with an extension, such as `2.3.5-SNAPSHOT` or `1.4-beta` (where `SNAPSHOT` and `beta` are extensions).

For each `Version` in the `v2`, `v3` and `v4` packages, there is a corresponding `ExtendedVersion`. `ExtendedVersion`s are constructed from `Version`s in the same way, regardless of whether they are `v2.Version`s, `v3.Version`s or `v4.Version`s.

### Extension Definitions

An `ExtensionDef` (`com.nthportal.versions.ExtensionDef`) is used to define the ordering of different extensions, as well as a default extension for versions without one (such as `1.2.5`).

An `ExtensionDef[E]` (for an extension of type `E`) contains:

- an `Ordering[E]`, defining the ordering of extensions
- an `Option[E]`, defining the default extension (for converting extensions to and from strings)
  - `Some(e)` means that `e` is the default extension
  - `None` means that there is no default extension

### Provided Extensions

This library provides two extensions in the `com.nthportal.versions.extensions` package:

- `Maven`, containing:
  - `Snapshot`
    - represents a snapshot version
    - usually denoted by the string 'SNAPSHOT'
  - `Release`
    - represents a release version
    - the default extension; has no string representation
- `AlphaBeta`, containing:
  - `preAlpha` ('pre-alpha')
  - `alpha` ('alpha')
  - `beta` ('beta')
  - `rc(X)` ('rc.X')
    - a release candidate, where `X` is the release candidate number
    - for example, `rc(4)` is represented by the string 'rc.4'  
  - `release` (default extension)

Importing the contents of either extension (e.g. `import com.nthportal.versions.extensions.Maven._`) brings an implicit `ExtensionDef` and an implicit `ExtensionParser` (discussed [later](#parsing-extendedversions)) for the extension into the scope.

### Creating Extended Versions

In order to create an `ExtendedVersion`, an implicit `ExtensionDef` for the desired extension must be in scope.

For purposes of the following example, `v3` versions will be used; however, the process is no different for `v2` and `v4`.

The version `1.2.5-SNAPSHOT` (of type `v3.ExtendedVersion[Maven]`) can be constructed in any of the following ways:

```scala
import com.nthportal.versions.v3._
import com.nthportal.versions.extensions.Maven._

Version(1, 2, 5) -- Snapshot
Version(1, 2, 5) dash Snapshot
Version of 1 dot 2 dot 5 dash Snapshot
```

The version `1.2.5` with the default (`Release`) extension must still be constructed explicitly with the extension, in one of the following ways:
 
```scala
import com.nthportal.versions.v3._
import com.nthportal.versions.extensions.Maven._

Version(1, 2, 5) -- Release
Version(1, 2, 5) dash Release
Version of 1 dot 2 dot 5 dash Release
```

### Parsing Extended Versions

In order to parse an `ExtendedVersion[E]` an implicit `ExtensionParser[E]` is required in the scope, as well as an implicit `ExtensionDef[E]`. As mentioned previously, importing the contents of either of the provided extensions brings the required implicits into scope.

As with a `Version`, an `ExtendedVersion` can be parsed from a string either directly as an `ExtendedVersion`, or as an `Option` (if the string might not represent a valid version).

An `ExtendedVersion[Maven]` can be parsed from a string as follows:

```scala
import com.nthportal.convert.Convert
import com.nthportal.versions.v3._
import com.nthportal.versions.extensions.Maven._

implicit val c = Convert.Throwing // or `Convert.AsOption`

// Version(1, 2, 5) -- Snapshot
ExtendedVersion.parseVersion("1.2.5-SNAPSHOT")

// Version(1, 2, 5) -- Release
ExtendedVersion.parseVersion("1.2.5")
```

### Extractors for Extended Versions

Fields can be extracted from an `ExtendedVersion` or a string, as follows:

```scala
import com.nthportal.versions._, v3._
import com.nthportal.versions.extensions.Maven._

// The second `case` statement in each of the following
// pattern matches will never be reached, but could replace
// the first
Version(1, 2, 5) -- Snapshot match {
  case version -- extension => // or
  case Version(major, minor, patch) -- extension =>
}
Version(1, 2, 5) dash Snapshot match {
  case version dash extension => // or
  case Version(major, minor, patch) dash extension =>
}
"1.2.5-SNAPSHOT" match {
  case ExtendedVersion(version, extension) => // or
  case ExtendedVersion(Version(major, minor, patch), extension) =>
}
```

### Extended Version Ordering

Extended versions are ordered by their version component, and then by their extension if their version components are the same.
 
Extended versions can only be compared to others of the same type (`v2`, `v3` or `v4`), with the same type of extension, and with the same extension ordering.

# Aliases

In order to reduce verbosity, each of the `v2`, `v3` and `v4` packages contains the following two aliases:

- `V`, an alias for `Version` (the object)
- `EV`, an alias for `ExtendedVersion` (the object)

Thus, the following pairs of statements are equivalent:

```scala
import com.nthportal.versions._, v3._
import com.nthportal.versions.extensions.Maven._
import com.nthportal.convert.Convert.Throwing.Implicit.ref // or `Convert.AsOption.Implicit.ref`

V(1, 2, 5) // is equivalent to
Version(1, 2, 5)

V of 1 dot 2 dot 5 // is equivalent to
Version of 1 dot 2 dot 5

V.parseVersion("1.2.5") // is equivalent to
Version.parseVersion("1.2.5")

EV.parseVersion("1.2.5-SNAPSHOT") // is equivalent to
ExtendedVersion.parseVersion("1.2.5-SNAPSHOT")

V(1, 2, 5) match { case V(major, minor, patch) => } // is equivalent to
Version(1, 2, 5) match { case Version(major, minor, patch) => }

"1.2.5" match { case V(major, minor, patch) => } // is equivalent to
"1.2.5" match { case Version(major, minor, patch) => }

V(1, 2, 5) -- Snapshot match { case V(major, minor, patch) -- extension => } // is equivalent to
Version(1, 2, 5) -- Snapshot match { case Version(major, minor, patch) -- extension => }

"1.2.5-SNAPSHOT" match { case EV(V(major, minor, patch), extension) => } // is equivalent to
"1.2.5-SNAPSHOT" match { case ExtendedVersion(Version(major, minor, patch), extension) => }
```

# SemVer

This library comes with support for [Semantic Versioning 2.0.0](http://semver.org/spec/v2.0.0.html) (SemVer) versions in the package `com.nthportal.versions.semver`.

### Creating SemVer Versions

The SemVer version `1.2.5-SNAPSHOT+commit.386bda5` can be created (from a `v3.ExtendedVersion`) in any of the following ways:

```scala
import com.nthportal.versions.v3._
import com.nthportal.versions.semver._
import com.nthportal.versions.extensions.Maven._

V(1, 2, 5) -- Snapshot + "commit.386bda5"
V(1, 2, 5) -- Snapshot withBuildMetadata "commit.386bda5"
V(1, 2, 5) -- Snapshot +? Some("commit.386bda5")
SemanticVersion(V(1, 2, 5) -- Snapshot, Some("commit.386bda5"))
```

The SemVer version `1.2.5-SNAPSHOT` (with no build metadata) can be created just as easily:

```scala
import com.nthportal.versions.v3._
import com.nthportal.versions.semver._
import com.nthportal.versions.extensions.Maven._

(V(1, 2, 5) -- Snapshot).withNoMetadata // optionally add `[M]` to specify the type of the non-existent metadata
V(1, 2, 5) -- Snapshot +? None
SemanticVersion(V(1, 2, 5) -- Snapshot, None)
```

### Parsing SemVer Versions

In order to parse a SemVer version from a string, all the implicits needed to parse an `ExtendedVersion` must be in scope, as well as an implicit `BuildMetadata.Parser[M]` (where `M` is the type of the metadata).

A simple `BuildMetadata.Parser[String]`, which merely takes any string as the metadata without parsing it, can be imported from `com.nthportal.versions.semver.BuildMetadata.stringMetadataParser`.

A SemVer version can be parsed from a string either directly as a `SemanticVersion`, or as an `Option` (if the string might not represent a valid SemVer version), as follows:

```scala
import com.nthportal.convert.Convert
import com.nthportal.versions.v3._
import com.nthportal.versions.semver._
import com.nthportal.versions.semver.BuildMetadata.stringMetadataParser
import com.nthportal.versions.extensions.Maven._

implicit val c = Convert.Throwing // or `Convert.AsOption`
parseSemVer("1.2.5-SNAPSHOT+commit.386bda5")
```

Additionally, if a SemVer version contains no metadata, it can be parsed directly as a `v3.ExtendedVersion`:

```scala
import com.nthportal.convert.Convert
import com.nthportal.versions.v3._
import com.nthportal.versions.semver._
import com.nthportal.versions.extensions.Maven._

implicit val c = Convert.Throwing // or `Convert.AsOption`
parseSemVerWithoutMetadata("1.2.5-SNAPSHOT")
```

Note that `parseSemVerWithoutMetadata` differs from `v3.ExtendedVersion.parseVersion` in that the specification for SemVer is stricter; consequently, some version strings which would be allowed by the latter are not allowed by the former.

### Convenience Extension Methods

For convenience, the methods `bumpMajor`, `bumpMinor` and `bumpPatch` are added (as implicit methods) for `v3.Version` and `v3.ExtendedVersion`:

- `bumpMajor`: increment the major version, and reset the minor and patch versions to `0`
- `bumpMinor`: increment the minor version, and reset the patch version to `0`
- `bumpPatch`: increment the patch version

### Extractors for SemVer Versions

Fields can be extracted from a SemVer version or a string as follows:

```scala
import com.nthportal.versions._, v3._
import com.nthportal.versions.semver._
import com.nthportal.versions.extensions.Maven._

// The second and third `case` statements will never
// be reached, but could replace the first
V(1, 2, 5) -- Snapshot + "commit.386bda5" match {
  case version -- extension + metadata =>
  case version -- extension +? Some(metadata) =>
  case SemanticVersion(version -- extension, Some(metadata)) =>
}

// The second `case` statement will never be reached,
// but could replace the first
V(1, 2, 5) -- Snapshot +? None match {
  case version -- extension +? None =>
  case SemanticVersion(version -- extension, None) =>
}

"1.2.5-SNAPSHOT+commit.386bda5" match {
  case SemVer(version, extension, Some(metadata)) =>
}

"1.2.5-SNAPSHOT" match {
  case SemVer(version, extension, None) =>
}
```

### SemVer Version Ordering

`SemanticVersion` ordering is the same as that of the `v3.ExtendedVersion` it contains; build metadata is ignored for purposes of ordering, regardless of whether or not it is present. This behavior is consistent with the [SemVer specification](http://semver.org/spec/v2.0.0.html#spec-item-10).
