package com.nthportal.versions
package variable

/**
  * A version with an extension (such as, for example, `1.2.5.4.1632-SNAPSHOT`)
  *
  * @param version      the version component of this extended version
  * @param extension    the extension component of this extended version
  * @param extensionDef the [[ExtensionDef extension definition]] for this version's extension
  * @tparam E the type of the extension component of this extended version
  */
final class ExtendedVersion[E](val version: Version, val extension: E, protected val extensionDef: ExtensionDef[E])
  extends ExtendedVersionBase[Version, E, ExtendedVersion] {
  override def equals(obj: Any) = obj match {
    case that: ExtendedVersion[_] =>
      this.version == that.version &&
        this.extension == that.extension &&
        this.extensionDef == that.extensionDef
    case _ => false
  }

  override def hashCode() = (version, extension, extensionDef).hashCode()
}

/**
  * [[ExtendedVersionCompanion Companion object]] for [[ExtendedVersion]],
  * which allows version [[Version.size sizes]] in the range `1 to 16`.
  */
object ExtendedVersion extends ExtendedVersions.From(Version)
