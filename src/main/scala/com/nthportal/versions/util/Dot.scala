package com.nthportal.versions.util

trait Dot[A] {
  def apply(value: Int): A

  final def dot(value: Int): A = apply(value)

  @inline
  final def :*(value: Int): A = dot(value)
}
