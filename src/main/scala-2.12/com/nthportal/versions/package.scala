package com.nthportal

package object versions {
  @inline
  private[versions] def _dot[A](f: Int => A): Dot[A] = f(_)

  @inline
  private[versions] def ordering[A](f: (A, A) => Int): Ordering[A] = f(_, _)
}
