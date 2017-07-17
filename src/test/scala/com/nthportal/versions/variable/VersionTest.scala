package com.nthportal.versions
package variable

class VersionTest extends SimpleSpec {
  behavior of "Version (variable)"

  it should "not allow negative version values" in {
    an [IllegalArgumentException] should be thrownBy { Version(-1, 0) }
    an [IllegalArgumentException] should be thrownBy { Version(0, -1, 0) }
    an [IllegalArgumentException] should be thrownBy { Version(0, 0, 0, -1) }
  }

  it should "return the correct size" in {
    Version(1, 3) should have size 2
    Version(1, 2, 5) should have size 3
    Version(1, 2, 5, 4) should have size 4
    Version(1, 2, 5, 4, 16) should have size 5
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

  it should "evaluate equality correctly" in {
    val v = Version(1, 2, 5)

    v shouldEqual Version(1, 2, 5)
    v.hashCode() shouldEqual Version(1, 2, 5).hashCode()

    v should not equal Version(1, 2, 6)
    v should not equal Version(1, 3)
    v should not equal Version(1, 2, 5, 4)
    v should not equal "a string"
  }

  it should "produce the correct string representation" in {
    Version(1).toString shouldBe "1"
    Version(1, 2, 5).toString shouldBe "1.2.5"
    Version(1, 2, 0, 20, 0).toString shouldBe "1.2.0.20.0"
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
