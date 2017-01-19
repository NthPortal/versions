package com.nthportal.versions

trait Of[D <: Dot[_]] {
  def apply(value: Int): D

  final def of(value: Int): D = apply(value)

  final def :>(value: Int): D = apply(value)
}
