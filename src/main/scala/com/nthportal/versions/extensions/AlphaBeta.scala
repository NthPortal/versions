package com.nthportal.versions
package extensions

/**
  * A version extension supporting pre-alpha, alpha, beta,
  * release-candidate, and release extensions.
  *
  * @param ord the order of the extension
  */
sealed abstract class AlphaBeta private(private val ord: Int) extends Ordered[AlphaBeta] {
  override def compare(that: AlphaBeta) = this.ord compare that.ord
}

/**
  * Companion object for the [[AlphaBeta]] version extension.
  *
  * Statically importing the contents of this object will put the necessary
  * implicits in scope for using extended versions of this type.
  *
  * @define ext AlphaBeta
  */
object AlphaBeta extends RichExtensionParser[AlphaBeta] {
  private val preAlphaToString = "pre-alpha"
  private val alphaToString = "alpha"
  private val betaToString = "beta"
  private val rcPrefix = "rc"

  /**
    * A pre-alpha version.
    */
  val preAlpha: AlphaBeta = new AlphaBeta(0) {
    override def toString = preAlphaToString
  }
  /**
    * An alpha version.
    */
  val alpha: AlphaBeta = new AlphaBeta(1) {
    override def toString = alphaToString
  }
  /**
    * A beta version.
    */
  val beta: AlphaBeta = new AlphaBeta(2) {
    override def toString = betaToString
  }
  /**
    * A release version.
    */
  val release: AlphaBeta = new AlphaBeta(4) {}

  /**
    * Returns a release candidate with the specified number.
    *
    * @param num the number of the release candidate
    * @return a release candidate with the specified number
    */
  def rc(num: Int): AlphaBeta = RC(num)

  private final case class RC(num: Int) extends AlphaBeta(3) {
    require(num >= 1, "release candidate number must be positive")

    override def compare(that: AlphaBeta) = that match {
      case RC(n) => num compare n
      case _ => super.compare(that)
    }

    override def toString = s"$rcPrefix.$num"
  }

  /**
    * The [[ExtensionDef extension definition]] for AlphaBeta extensions.
    */
  implicit val extensionDef: ExtensionDef[AlphaBeta] = ExtensionDef.fromOrdered(release)

  @throws[IllegalArgumentException]
  override def parse(extension: String): AlphaBeta = extension match {
    case this.preAlphaToString => preAlpha
    case this.alphaToString => alpha
    case this.betaToString => beta
    case e => e split '.' match {
      case Array(this.rcPrefix, num) =>
        try {
          RC(num.toInt)
        } catch {
          case e: IllegalArgumentException => invalidExtension(extension, e)
        }
      case _ => invalidExtension(extension)
    }
  }
}
