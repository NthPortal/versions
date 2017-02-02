package com.nthportal

package object versions {
  @inline
  private[versions] def _dot[A](f: Int => A): Dot[A] = new Dot[A] {override def apply(value: Int): A = f(value)}

  @inline
  private[versions] def ordering[A](f: (A, A) => Int): Ordering[A] = new Ordering[A] {
    override def compare(x: A, y: A): Int = f(x, y)
  }
}
