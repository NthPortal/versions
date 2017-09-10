package com.nthportal.versions
package v2

import com.nthportal.convert.Convert

class VersionTest extends SimpleSpec {

  behavior of "Version (2)"

  it should "have consistent constructors" in {
    val v = Version(1)(3)
    v should equal (Version of 1 dot 3)
    v shouldEqual Version(1, 3)
  }

  it should "not allow negative version values" in {
    an [IllegalArgumentException] should be thrownBy {Version(-1)(0)}
    an [IllegalArgumentException] should be thrownBy {Version(0)(-1)}
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
    import Convert.Valid.Implicit.ref

    Version parseVersion "1.3" shouldEqual Version(1)(3)
    Version parseVersion "0.0" shouldEqual Version(0)(0)

    a [VersionFormatException] should be thrownBy {Version parseVersion "1.0."}
    a [VersionFormatException] should be thrownBy {Version parseVersion "-1.0"}
    a [VersionFormatException] should be thrownBy {Version parseVersion "1"}
    a [VersionFormatException] should be thrownBy {Version parseVersion "1.0.0"}
    a [VersionFormatException] should be thrownBy {Version parseVersion "1.f"}
    a [VersionFormatException] should be thrownBy {Version parseVersion "really not a version"}
  }

  it should "parse versions as options correctly" in {
    import Convert.Any.Implicit.ref

    Version.parseVersion("1.3").value shouldEqual Version(1)(3)
    Version.parseVersion("0.0").value shouldEqual Version(0)(0)

    Version parseVersion "1.0." shouldBe empty
    Version parseVersion "-1.0" shouldBe empty
    Version parseVersion "1" shouldBe empty
    Version parseVersion "1.0.0" shouldBe empty
    Version parseVersion "1.f" shouldBe empty
    Version parseVersion "really not a version" shouldBe empty
  }

  it should "pattern match versions correctly" in {
    inside("1.3") { case Version(1, 3) => }
    inside("0.0") { case Version(0, 0) => }

    Version unapply "1.0." shouldBe empty
    Version unapply "-1.0" shouldBe empty
    Version unapply "1" shouldBe empty
    Version unapply "1.0.0" shouldBe empty
    Version unapply "1.f" shouldBe empty
    Version unapply "really not a version" shouldBe empty
  }

  it should "convert to other types correctly" in {
    import Convert.Valid.Implicit.ref
    val v = Version(1, 3)

    v to Version shouldEqual v
    v to variable.Version shouldEqual variable.Version(1, 3)

    an [IllegalArgumentException] should be thrownBy { v to v3.Version }
    an [IllegalArgumentException] should be thrownBy { v to v4.Version }
  }

  it should "convert as an option to other types correctly" in {
    import Convert.Any.Implicit.ref
    val v = Version(1, 3)

    v.to(Version).value shouldEqual v
    v.to(variable.Version).value shouldEqual variable.Version(1, 3)

    v to v3.Version shouldBe empty
    v to v4.Version shouldBe empty
  }
}
