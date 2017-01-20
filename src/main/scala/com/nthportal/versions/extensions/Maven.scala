package com.nthportal.versions
package extensions

/**
  * A simple [[https://maven.apache.org/ Maven]] version extension,
  * supporting snapshot and release types.
  *
  * @param ord the order of the extension
  */
sealed abstract class Maven(private val ord: Int) extends Ordered[Maven] {
  override def compare(that: Maven) = this.ord compare that.ord
}

/**
  * Companion object for the [[Maven]] version extension.
  *
  * Statically importing the contents of this object will put the necessary
  * implicits in scope for using extended versions of this type.
  */
object Maven extends ExtensionParser[Maven] {
  /**
    * The [[ExtensionDef extension definition]] for Maven extensions.
    */
  implicit val extensionDef: ExtensionDef[Maven] = ExtensionDef.fromOrdered[Maven](Release)

  /**
    * The parser for Maven extensions.
    *
    * @return the parser for Maven extensions
    */
  implicit def parser: ExtensionParser[Maven] = this

  /**
    * A snapshot.
    */
  case object Snapshot extends Maven(0) {
    override val toString = "SNAPSHOT"
  }

  /**
    * A release.
    */
  case object Release extends Maven(1)

  @throws[IllegalArgumentException]
  override def parse(extension: String): Maven = extension match {
    case Snapshot.toString => Snapshot
    case _ => throw new IllegalArgumentException(s"Invalid extension: $extension")
  }
}
