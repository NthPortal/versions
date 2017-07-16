package com.nthportal.versions

/**
  * Indicates that a value can be appended to this to create a
  * version or something which can become a version.
  *
  * @tparam A the type returned which is or can become a version
  */
trait Dot[A] {
  /**
    * Creates a version or something which can become a version by
    * appending the given value to this.
    *
    * For example, if this is `2`, the result of this method invoked
    * as `apply(3)` would be `2.3`.
    *
    * @param value the value to append
    * @return a version or something which can become a version
    */
  def apply(value: Int): A

  /**
    * @see [[apply]]
    */
  final def dot(value: Int): A = apply(value)
}
