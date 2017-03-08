package com.nthportal

package object versions {
  @inline
  private[versions] def _dot[A](f: Int => A): Dot[A] = f(_)

  @inline
  private[versions] def _ordering[A](f: (A, A) => Int): Ordering[A] = f(_, _)

  @inline
  private[versions] def _extensionParser[E](f: String => E): ExtensionParser[E] = f(_)
}
