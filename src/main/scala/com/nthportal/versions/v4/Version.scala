package com.nthportal
package versions
package v4

/**
  * A version of the form `_1`.`_2`.`_3`.`_4` (such as, for example, `1.2.5.4`).
  *
  * @param _1 the first value of the version
  * @param _2 the second value of the version
  * @param _3 the third value of the version
  * @param _4 the fourth value of the version
  */
final case class Version(_1: Int, _2: Int, _3: Int, _4: Int) extends VersionBase[Version, ExtendedVersion] {
  // Validate values
  require(_1 >= 0 && _2 >= 0 && _3 >= 0 && _4 >= 0, "_1, _2, _3, and _4 values must all be >= 0")

  override protected def companion = Version

  override protected def extendedCompanion = ExtendedVersion

  override protected def toSeq: Seq[Int] = Seq(_1, _2, _3, _4)

  override def toString = s"${_1}.${_2}.${_3}.${_4}"
}

object Version extends VersionCompanion[Version, ExtendedVersion] with Of[Dot[Dot[Dot[Version]]]] {
  override private[versions] val ordering: Ordering[Version] =
    Ordering.by[Version, Int](_._1)
      .thenBy(_._2)
      .thenBy(_._3)
      .thenBy(_._4)

  override def of(_1: Int): Dot[Dot[Dot[Version]]] = _2 => _3 => _4 => apply(_1, _2, _3, _4)

  override protected[versions] def versionFromSeq = { case Seq(_1, _2, _3, _4) => apply(_1, _2, _3, _4) }

  /**
    * Extracts a version from a string.
    *
    * @param version the string from which to extract a version
    * @return an [[Option]] containing the four version numbers;
    *         [[None]] if the string did not represent a valid version
    */
  def unapply(version: String): Option[(Int, Int, Int, Int)] = parseAsOption(version) flatMap unapply
}
