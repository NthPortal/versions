package com.nthportal.versions
package v3

class VersionTest extends SimpleSpec {

  behavior of "Version (3)"

  it should "have consistent constructors" in {
    val v = Version(1)(2)(5)
    v should equal (Version of 1 dot 2 dot 5)
    v should equal (Version(1, 2, 5))
  }

  it should "not allow negative version values" in {
    an [IllegalArgumentException] should be thrownBy {Version(-1)(0)(0)}
    an [IllegalArgumentException] should be thrownBy {Version(0)(-1)(0)}
    an [IllegalArgumentException] should be thrownBy {Version(0)(0)(-1)}
  }

  it should "compare correctly" in {
    val v = Version(1)(2)(5)

    v should be > Version(1)(2)(4)
    v should be > Version(1)(1)(9)
    v should be > Version(0)(9)(9)

    v should be < Version(1)(2)(6)
    v should be < Version(1)(3)(0)
    v should be < Version(2)(0)(0)
  }

  it should "produce the correct string representation" in {
    Version(1)(2)(5).toString should be ("1.2.5")
  }

  it should "parse versions correctly" in {
    Version parseVersion "1.2.5" shouldEqual Version(1)(2)(5)
    Version parseVersion "0.0.0" shouldEqual Version(0)(0)(0)

    a [VersionFormatException] should be thrownBy {Version parseVersion "-1.0.0"}
    a [VersionFormatException] should be thrownBy {Version parseVersion "1.0"}
    a [VersionFormatException] should be thrownBy {Version parseVersion "1.0.0.0"}
    a [VersionFormatException] should be thrownBy {Version parseVersion "1.f.0"}
    a [VersionFormatException] should be thrownBy {Version parseVersion "really not a version"}
  }

  it should "unapply versions correctly" in {
    Version unapply "1.2.5" shouldEqual Some((1, 2, 5))
    Version unapply "0.0.0" shouldEqual Some((0, 0, 0))

    Version unapply "-1.0.0" shouldBe empty
    Version unapply "1.0" shouldBe empty
    Version unapply "1.0.0.0" shouldBe empty
    Version unapply "1.f.0" shouldBe empty
    Version unapply "really not a version" shouldBe empty
  }
}
