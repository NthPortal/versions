package com.nthportal.versions

trait Of[D <: Dot[_]] {
  def of(value: Int): D

  final def apply(value: Int): D = of(value)

  final def :>(value: Int): D = of(value)
}
