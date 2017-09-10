package com.nthportal.versions
package extensions

import com.nthportal.convert.Convert

/**
  * A simple [[https://maven.apache.org/ Maven]] version extension,
  * supporting snapshot and release types.
  *
  * @param ord the order of the extension
  */
sealed abstract class Maven private(private val ord: Int) extends Ordered[Maven] {
  override def compare(that: Maven) = this.ord compare that.ord
}

/**
  * Companion object for the [[Maven]] version extension.
  *
  * Statically importing the contents of this object will put the necessary
  * implicits in scope for using extended versions of this type.
  *
  * @define ext Maven
  */
object Maven extends RichExtensionParser[Maven] {
  private val snapshotToStr = "SNAPSHOT"

  /**
    * A snapshot.
    */
  val Snapshot: Maven = new Maven(0) {
    override def toString = snapshotToStr
  }

  /**
    * A release.
    */
  val Release: Maven = new Maven(1) {}

  /**
    * The [[ExtensionDef extension definition]] for Maven extensions.
    */
  implicit val extensionDef: ExtensionDef[Maven] = ExtensionDef.fromOrdered[Maven](Release)

  @deprecated("use `extensionParser` instead", since = "1.1.0")
  def parser: ExtensionParser[Maven] = this

  @throws[IllegalArgumentException]
  override def parse(extension: String)(implicit c: Convert): c.Result[Maven] = {
    import c._
    conversion {
      extension match {
        case this.snapshotToStr => Snapshot
        case _ => invalidExtension(extension)
      }
    }
  }
}
