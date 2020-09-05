package com.nthportal.versions

/**
 * Indicates that a value can be used by this to start creating a version.
 *
 * @tparam D the type returned by this which can become a version
 */
trait Of[D <: Dot[_]] {

  /**
   * Creates something which can become a version starting with the given value.
   *
   * @param value the first value for the version.
   * @return something which can become a version starting with the given value
   */
  def of(value: Int): D

  /**
   * @see [[of]]
   */
  final def apply(value: Int): D = of(value)
}
