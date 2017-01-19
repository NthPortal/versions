package com.nthportal.versions
package extensions

sealed abstract class Maven(private val ord: Int) extends Ordered[Maven] {
  override def compare(that: Maven) = this.ord compare that.ord
}

object Maven {
  implicit val extensionDef: ExtensionDef[Maven] = ExtensionDef(Some(Release), _ compare _)

  case object Snapshot extends Maven(0)

  case object Release extends Maven(1)

}
