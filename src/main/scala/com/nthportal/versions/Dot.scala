package com.nthportal.versions

trait Dot[A] {
  def apply(value: Int): A

  final def dot(value: Int): A = apply(value)

  final def :*(value: Int): A = apply(value)
}
