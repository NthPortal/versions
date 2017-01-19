package com.nthportal.versions
package extensions

sealed abstract class Maven(private val ord: Int) extends Ordered[Maven] {
  override def compare(that: Maven) = this.ord compare that.ord
}

object Maven extends ExtensionParser[Maven] {
  implicit val extensionDef: ExtensionDef[Maven] = ExtensionDef(Some(Release), _ compare _)

  implicit val parseDef: ExtensionParseDef[Maven] = ExtensionParseDef(extensionDef, this)

  case object Snapshot extends Maven(0)

  case object Release extends Maven(1)

  @throws[IllegalArgumentException]
  override def parse(extension: String): Maven = extension match {
    case "SNAPSHOT" => Snapshot
    case _ => throw new IllegalArgumentException(s"Invalid extension: $extension")
  }
}
