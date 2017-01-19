package com.nthportal.versions.v3

import com.nthportal.versions.util.Dot

case class Version(major: Int, minor: Int, patch: Int) extends Ordered[Version] {
  // Validate values
  require(major >= 0 && minor >= 0 && patch >= 0, "major, minor, and patch values must all be >= 0")

  override def compare(that: Version): Int = Version.ordering.compare(this, that)
}

object Version {
  private val ordering: Ordering[Version] = Ordering by (v => (v.major, v.minor, v.patch))

  def apply(major: Int): Dot[Dot[Version]] = (minor: Int) => (patch: Int) => apply(major, minor, patch)

  def :>(major: Int): Dot[Dot[Version]] = apply(major)
}
