package com.nthportal.versions
package variable

class VersionTest extends SimpleSpec {
  behavior of "Version (variable)"

  it should "not allow negative version values" in {
    an [IllegalArgumentException] should be thrownBy { Version(-1, 0) }
    an [IllegalArgumentException] should be thrownBy { Version(0, -1, 0) }
    an [IllegalArgumentException] should be thrownBy { Version(0, 0, 0, -1) }
  }

  it should "compare correctly" in {
    val v1 = Version(1, 2, 5)

    v1 should be > Version(1, 2, 4)
    v1 should be > Version(1, 1, 9)
    v1 should be > Version(0, 9, 9)

    v1 should be < Version(1, 2, 6)
    v1 should be < Version(1, 3, 0)
    v1 should be < Version(2, 0, 0)

    val v2 = Version(1, 2, 0)

    v2 should be > Version(1, 2)
    v2 should be < Version(1, 2, 0, 0)
  }

  it should "produce the correct string representation" in {
    Version(1).toString shouldBe "1"
    Version(1, 2, 5).toString shouldBe "1.2.5"
    Version(1, 2, 0, 20, 0).toString shouldBe "1.2.0.20.0"
  }

  it should "parse versions correctly" in {
    Version parseVersion "1.2.5" shouldEqual Version(1, 2, 5)
    Version parseVersion "0.0" shouldEqual Version(0, 0)
    Version parseVersion "12" shouldEqual Version(12)
    Version parseVersion "1.2.0.20.0" shouldEqual Version(1, 2, 0, 20, 0)

    a [VersionFormatException] should be thrownBy {Version parseVersion "1.0."}
    a [VersionFormatException] should be thrownBy {Version parseVersion "-1.0"}
    a [VersionFormatException] should be thrownBy {Version parseVersion "1.f"}
    a [VersionFormatException] should be thrownBy {Version parseVersion "really not a version"}
  }

  it should "parse versions as options correctly" in {
    Version.parseAsOption("1.2.5").value shouldEqual Version(1, 2, 5)
    Version.parseAsOption("0.0").value shouldEqual Version(0, 0)
    Version.parseAsOption("12").value shouldEqual Version(12)
    Version.parseAsOption("1.2.0.20.0").value shouldEqual Version(1, 2, 0, 20, 0)

    Version parseAsOption "1.0." shouldBe empty
    Version parseAsOption "-1.0" shouldBe empty
    Version parseAsOption "1.f" shouldBe empty
    Version parseAsOption "really not a version" shouldBe empty
  }

  it should "pattern match versions correctly" in {
    inside("1.2.5") { case Version(1, 2, 5) => }
    inside("0.0") { case Version(0, 0) => }
    inside("12") { case Version(12) => }
    inside("1.2.0.20.0") { case Version(1, 2, 0, 20, 0) => }

    Version unapplySeq "1.0." shouldBe empty
    Version unapplySeq "-1.0" shouldBe empty
    Version unapplySeq "1.f" shouldBe empty
    Version unapplySeq "really not a version" shouldBe empty
  }

  it should "convert to other types correctly" in {
    Version(1, 2, 5, 4, 16).to(Version).value shouldEqual Version(1, 2, 5, 4, 16)
    Version(1, 3).to(v2.Version).value shouldEqual v2.Version(1, 3)
    Version(1, 2, 5).to(v3.Version).value shouldEqual v3.Version(1, 2, 5)
    Version(1, 2, 5, 4).to(v4.Version).value shouldEqual v4.Version(1, 2, 5, 4)

    val v = Version(1)

    v.to(Version).value shouldEqual v
    v.to(v2.Version) shouldBe empty
    v.to(v3.Version) shouldBe empty
    v.to(v4.Version) shouldBe empty
  }
}
