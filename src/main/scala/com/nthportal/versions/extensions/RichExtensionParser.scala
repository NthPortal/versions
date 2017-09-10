package com.nthportal.versions
package extensions

import com.nthportal.convert.Convert

/**
  * An [[ExtensionParser]] with extra utility methods.
  *
  * @tparam E the type of the version extension
  * @define ext E
  */
private[extensions] trait RichExtensionParser[E] extends ExtensionParser[E] {
  /**
    * Returns a parser for $ext extensions.
    *
    * @return a parser for $ext extensions
    */
  implicit def extensionParser: ExtensionParser[E] = this

  /**
    * Throws an [[scala.IllegalArgumentException]] for an invalid extension.
    *
    * @param extension the invalid extension string
    * @param cause the exception making the extension invalid (`null` if none)
    * @throws scala.IllegalArgumentException always
    * @return Nothing
    */
  @inline
  @throws[IllegalArgumentException]
  protected def invalidExtension(extension: String, cause: Throwable = null)(implicit c: Convert): Nothing = {
    c.fail(new IllegalArgumentException(s"Invalid extension: $extension", cause))
  }
}
