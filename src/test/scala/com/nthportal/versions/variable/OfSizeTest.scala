package com.nthportal.versions
package variable

class OfSizeTest extends SimpleSpec {
  behavior of "OfSize"

  it should "only allow valid sizes" in {
    noException should be thrownBy { Versions.ofSize(1 to 10) }

    an [IllegalArgumentException] should be thrownBy { Versions.ofSize(2 until 2) }
    an [IllegalArgumentException] should be thrownBy { Versions.ofSize(0 to 5) }
  }

  it should "create versions correctly" in {
    val ver = Versions.ofSize(1 to 2)

    noException should be thrownBy { ver(1, 3) }
    noException should be thrownBy { ver(0, 0) }
    noException should be thrownBy { ver(12) }

    an [IllegalArgumentException] should be thrownBy { ver() }
    an [IllegalArgumentException] should be thrownBy { ver(1, 2, 5) }
  }

  it should "parse versions correctly" in {
    val ver = Versions.ofSize(1 to 2)

    ver parseVersion "1.3" shouldEqual ver(1, 3)
    ver parseVersion "0.0" shouldEqual ver(0, 0)
    ver parseVersion "12" shouldEqual ver(12)

    a [VersionFormatException] should be thrownBy { ver parseVersion "" }
    a [VersionFormatException] should be thrownBy { ver parseVersion "1.2.5" }
    a [VersionFormatException] should be thrownBy { ver parseVersion "1.0." }
    a [VersionFormatException] should be thrownBy { ver parseVersion "-1.0" }
    a [VersionFormatException] should be thrownBy { ver parseVersion "1.f" }
    a [VersionFormatException] should be thrownBy { ver parseVersion "really not a version" }
  }

  it should "parse versions as options correctly" in {
    val ver = Versions.ofSize(1 to 2)

    ver.parseAsOption("1.3").value shouldEqual ver(1, 3)
    ver.parseAsOption("0.0").value shouldEqual ver(0, 0)
    ver.parseAsOption("12").value shouldEqual ver(12)

    ver parseAsOption "" shouldBe empty
    ver parseAsOption "1.2.5" shouldBe empty
    ver parseAsOption "1.0." shouldBe empty
    ver parseAsOption "-1.0" shouldBe empty
    ver parseAsOption "1.f" shouldBe empty
    ver parseAsOption "really not a version" shouldBe empty
  }

  it should "pattern match versions correctly" in {
    val ver = Versions.ofSize(2 to 3)

    inside(ver(1, 2, 5)) { case ver(1, 2, 5) => }
    inside(ver(0, 0, 0)) { case ver(0, 0, 0) => }
    inside(ver(1, 3)) { case ver(1, 3) => }

    ver unapplySeq Version(1) shouldBe empty
    ver unapplySeq Version(1, 2, 5, 4) shouldBe empty
  }

  it should "pattern match version strings correctly" in {
    val ver = Versions.ofSize(1 to 2)

    inside("1.3") { case ver(1, 3) => }
    inside("0.0") { case ver(0, 0) => }
    inside("12") { case ver(12) => }

    ver unapplySeq "" shouldBe empty
    ver unapplySeq "1.2.5" shouldBe empty
    ver unapplySeq "1.0." shouldBe empty
    ver unapplySeq "-1.0" shouldBe empty
    ver unapplySeq "1.f" shouldBe empty
    ver unapplySeq "really not a version" shouldBe empty
  }
}
