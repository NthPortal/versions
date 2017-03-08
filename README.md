# versions
A Scala library for representing versions as objects

[![Build Status](https://travis-ci.org/NthPortal/versions.svg?branch=master)](https://travis-ci.org/NthPortal/versions)
[![Coverage Status](https://coveralls.io/repos/github/NthPortal/versions/badge.svg?branch=master)](https://coveralls.io/github/NthPortal/versions?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/com.nthportal/versions_2.12.svg)](https://mvnrepository.com/artifact/com.nthportal/versions_2.12)
[![Versioning](https://img.shields.io/badge/versioning-semver%202.0.0-blue.svg)](http://semver.org/spec/v2.0.0.html)

## Add as a Dependency

### SBT (Scala 2.11 and 2.12)

```sbt
"com.nthportal" %% "versions" % "1.2.1"
```

### Maven

**Scala 2.12**

```xml
<dependency>
  <groupId>com.nthportal</groupId>
  <artifactId>versions_2.12</artifactId>
  <version>1.2.1</version>
</dependency>
```

**Scala 2.11**

```xml
<dependency>
  <groupId>com.nthportal</groupId>
  <artifactId>versions_2.11</artifactId>
  <version>1.2.1</version>
</dependency>
```

## Examples

#### `v3.Version` and `v3.ExtendedVersion`

```scala
import com.nthportal.versions.v3.{Version, ExtendedVersion}

// Create a Version ('1.2.5')
val v1 = Version(1, 2, 5)
val v2 = Version(1)(2)(5)
val v3 = Version of 1 dot 2 dot 5

// Parse a version from a string
val v4 = Version.parseVersion("1.2.5")
assert(v4 == v1)

// Version with Maven extension ('1.2.5-SNAPSHOT')
import com.nthportal.versions.extensions.Maven._
val ev1 = v1 -- Snapshot
val ev2 = v1 dash Snapshot
val ev3 = Version(1)(2)(5) -- Snapshot
val ev4 = ExtendedVersion.parseVersion("1.2.5-SNAPSHOT")
assert(ev1 == ev4)

// Version with Maven extension (release '1.2.5')
val ev5 = v1 -- Release
val ev6 = ExtendedVersion.parseVersion("1.2.5")
assert(ev5 == ev6)
```

#### `v2.Version`

```scala
import com.nthportal.versions.v2.Version

// Create a Version ('1.3')
val v1 = Version(1, 3)
val v2 = Version(1)(3)
val v3 = Version of 1 dot 3

// Parse a version from a string
val v4 = Version.parseVersion("1.3")
assert(v4 == v1)
```

#### `v4.Version`

```scala
import com.nthportal.versions.v4.Version

// Create a Version ('1.2.4.6')
val v1 = Version(1, 2, 4, 6)
val v2 = Version(1)(2)(4)(6)
val v3 = Version of 1 dot 2 dot 4 dot 6

// Parse a version from a string
val v4 = Version.parseVersion("1.2.4.6")
assert(v4 == v1)
```

#### SemVer

```scala
import com.nthportal.versions.extensions.Maven._
import com.nthportal.versions.semver._
import com.nthportal.versions.v3.{Version, ExtendedVersion}

// Create a version with build metadata ('1.2.5-SNAPSHOT+commit.48e5a2e')
val sv1 = Version(1)(2)(5) -- Snapshot + "commit.48e5a2e"
val sv2 = Version(1)(2)(5) -- Snapshot withBuildMetadata "commit.48e5a2e"

// Create a version with no build metadata ('1.2.5-SNAPSHOT')
val sv3 = (Version(1)(2)(5) -- Snapshot).withNoMetadata[String]

// Parse a SemVer version from a string, ignoring build metadata
val ev1 = parseSemVerVersion("1.2.5-SNAPSHOT")
val ev2 = parseSemVerVersion("1.2.5-SNAPSHOT+commit.48e5a2e")
assert(ev1 == Version(1)(2)(5) -- Snapshot)
assert(ev2 == ev1)

// Parse a SemVer version from a string
import com.nthportal.versions.semver.BuildMetadata.stringMetadataParser
val sv4 = parseSemVerWithBuildMetadata("1.2.5-SNAPSHOT")
val sv5 = parseSemVerWithBuildMetadata("1.2.5-SNAPSHOT+commit.48e5a2e")
assert(sv4 == sv3)
assert(sv5 == sv1)

// Version bumping
assert(Version(1)(2)(5).bumpPatch == Version(1)(2)(6))
assert(Version(1)(2)(5).bumpMinor == Version(1)(3)(0))
assert(Version(1)(2)(5).bumpMajor == Version(2)(0)(0))

// ExtendedVersion bumping
assert((Version(1)(2)(5) -- Snapshot).bumpPatch == Version(1)(2)(6) -- Snapshot)
assert((Version(1)(2)(5) -- Snapshot).bumpMinor == Version(1)(3)(0) -- Snapshot)
assert((Version(1)(2)(5) -- Snapshot).bumpMajor == Version(2)(0)(0) -- Snapshot)
```
