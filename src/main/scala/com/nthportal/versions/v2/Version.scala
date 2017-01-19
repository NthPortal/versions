package com.nthportal.versions.v2

import com.nthportal.versions.util.Dot

case class Version(major: Int, minor: Int) extends Ordered[Version] {
  // Validate values
  require(major >= 0 && minor >= 0, "major and minor values must be >= 0")

  override def compare(that: Version): Int = Version.ordering.compare(this, that)
}

object Version {
  private val ordering: Ordering[Version] = Ordering by (v => (v.major, v.minor,))

  def apply(major: Int): Dot[Version] = (minor: Int) => apply(major, minor)

  def :>(major: Int): Dot[Version] = apply(major)
}
