package com.nthportal.versions
package v2

class VersionTest extends SimpleSpec {

  behavior of "Version (2)"

  it should "have consistent constructors" in {
    val v = Version(1)(3)
    v should equal(Version of 1 dot 3)
    v shouldEqual Version(1, 3)
  }

  it should "not allow negative version values" in {
    an[IllegalArgumentException] should be thrownBy { Version(-1)(0) }
    an[IllegalArgumentException] should be thrownBy { Version(0)(-1) }
  }

  it should "compare correctly" in {
    val v = Version(1)(3)

    v should be > Version(1)(2)
    v should be > Version(0)(9)

    v should be < Version(1)(4)
    v should be < Version(2)(0)
  }

  it should "produce the correct string representation" in {
    Version(1)(3).toString shouldBe "1.3"
  }

  it should "parse versions correctly" in {
    Version parseVersion "1.3" shouldEqual Version(1)(3)
    Version parseVersion "0.0" shouldEqual Version(0)(0)

    a[VersionFormatException] should be thrownBy { Version parseVersion "1.0." }
    a[VersionFormatException] should be thrownBy { Version parseVersion "-1.0" }
    a[VersionFormatException] should be thrownBy { Version parseVersion "1" }
    a[VersionFormatException] should be thrownBy { Version parseVersion "1.0.0" }
    a[VersionFormatException] should be thrownBy { Version parseVersion "1.f" }
    a[VersionFormatException] should be thrownBy { Version parseVersion "really not a version" }
  }

  it should "parse versions as options correctly" in {
    Version.parseAsOption("1.3").value shouldEqual Version(1)(3)
    Version.parseAsOption("0.0").value shouldEqual Version(0)(0)

    Version parseAsOption "1.0." shouldBe empty
    Version parseAsOption "-1.0" shouldBe empty
    Version parseAsOption "1" shouldBe empty
    Version parseAsOption "1.0.0" shouldBe empty
    Version parseAsOption "1.f" shouldBe empty
    Version parseAsOption "really not a version" shouldBe empty
  }

  it should "pattern match versions correctly" in {
    inside("1.3") { case Version(1, 3) => }
    inside("0.0") { case Version(0, 0) => }

    Version unapply "-1.0" shouldBe empty
    Version unapply "1" shouldBe empty
    Version unapply "1.0.0" shouldBe empty
    Version unapply "1.f" shouldBe empty
    Version unapply "really not a version" shouldBe empty
  }
}
