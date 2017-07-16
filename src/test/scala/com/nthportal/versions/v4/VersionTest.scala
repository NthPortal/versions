package com.nthportal.versions
package v4

class VersionTest extends SimpleSpec {

  behavior of "Version (4)"

  it should "have consistent constructors" in {
    val v = Version(1)(2)(5)(4)
    v should equal (Version of 1 dot 2 dot 5 dot 4)
    v shouldEqual Version(1, 2, 5, 4)
  }

  it should "not allow negative version values" in {
    an [IllegalArgumentException] should be thrownBy {Version(-1)(0)(0)(0)}
    an [IllegalArgumentException] should be thrownBy {Version(0)(-1)(0)(0)}
    an [IllegalArgumentException] should be thrownBy {Version(0)(0)(-1)(0)}
    an [IllegalArgumentException] should be thrownBy {Version(0)(0)(0)(-1)}
  }

  it should "compare correctly" in {
    val v = Version(1)(2)(5)(4)

    v should be > Version(1)(2)(5)(3)
    v should be > Version(1)(2)(4)(9)
    v should be > Version(1)(1)(9)(9)
    v should be > Version(0)(9)(9)(9)

    v should be < Version(1)(2)(5)(5)
    v should be < Version(1)(2)(6)(0)
    v should be < Version(1)(3)(0)(0)
    v should be < Version(2)(0)(0)(0)
  }

  it should "produce the correct string representation" in {
    Version(1)(2)(5)(4).toString shouldBe "1.2.5.4"
  }

  it should "parse versions correctly" in {
    Version parseVersion "1.2.5.4" shouldEqual Version(1)(2)(5)(4)
    Version parseVersion "0.0.0.0" shouldEqual Version(0)(0)(0)(0)

    a [VersionFormatException] should be thrownBy {Version parseVersion "1.0.0.0."}
    a [VersionFormatException] should be thrownBy {Version parseVersion "-1.0.0.0"}
    a [VersionFormatException] should be thrownBy {Version parseVersion "1.0.0"}
    a [VersionFormatException] should be thrownBy {Version parseVersion "1.0.0.0.0"}
    a [VersionFormatException] should be thrownBy {Version parseVersion "1.f.0.0"}
    a [VersionFormatException] should be thrownBy {Version parseVersion "really not a version"}
  }

  it should "parse versions as options correctly" in {
    Version.parseAsOption("1.2.5.4").value shouldEqual Version(1)(2)(5)(4)
    Version.parseAsOption("0.0.0.0").value shouldEqual Version(0)(0)(0)(0)

    Version parseAsOption "1.0.0.0." shouldBe empty
    Version parseAsOption "-1.0.0.0" shouldBe empty
    Version parseAsOption "1.0.0" shouldBe empty
    Version parseAsOption "1.0.0.0.0" shouldBe empty
    Version parseAsOption "1.f.0.0" shouldBe empty
    Version parseAsOption "really not a version" shouldBe empty
  }

  it should "pattern match versions correctly" in {
    inside("1.2.5.4") { case Version(1, 2, 5, 4) => }
    inside("0.0.0.0") { case Version(0, 0, 0, 0) => }

    Version unapply "-1.0.0.0" shouldBe empty
    Version unapply "1.0.0" shouldBe empty
    Version unapply "1.0.0.0.0" shouldBe empty
    Version unapply "1.f.0.0" shouldBe empty
    Version unapply "really not a version" shouldBe empty
  }
}
