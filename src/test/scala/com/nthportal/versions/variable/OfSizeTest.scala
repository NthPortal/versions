package com.nthportal.versions
package variable

import com.nthportal.convert.Convert

class OfSizeTest extends SimpleSpec {
  behavior of "Version.ofSize"

  it should "only allow valid sizes" in {
    noException should be thrownBy { Version.ofSize(1 to 10) }

    an [IllegalArgumentException] should be thrownBy { Version.ofSize(2 until 2) }
    an [IllegalArgumentException] should be thrownBy { Version.ofSize(0 to 5) }
  }

  it should "create versions correctly" in {
    val V = Version.ofSize(1 to 2)

    noException should be thrownBy { V(1, 3) }
    noException should be thrownBy { V(0, 0) }
    noException should be thrownBy { V(12) }

    an [IllegalArgumentException] should be thrownBy { V() }
    an [IllegalArgumentException] should be thrownBy { V(1, 2, 5) }
  }

  it should "parse versions correctly" in {
    import Convert.Throwing.Implicit.ref
    val V = Version.ofSize(1 to 2)

    V parseVersion "1.3" shouldEqual V(1, 3)
    V parseVersion "0.0" shouldEqual V(0, 0)
    V parseVersion "12" shouldEqual V(12)

    a [VersionFormatException] should be thrownBy { V parseVersion "" }
    a [VersionFormatException] should be thrownBy { V parseVersion "1.2.5" }
    a [VersionFormatException] should be thrownBy { V parseVersion "1.0." }
    a [VersionFormatException] should be thrownBy { V parseVersion "-1.0" }
    a [VersionFormatException] should be thrownBy { V parseVersion "1.f" }
    a [VersionFormatException] should be thrownBy { V parseVersion "really not a version" }
  }

  it should "parse versions as options correctly" in {
    import Convert.AsOption.Implicit.ref
    val V = Version.ofSize(1 to 2)

    V.parseVersion("1.3").value shouldEqual V(1, 3)
    V.parseVersion("0.0").value shouldEqual V(0, 0)
    V.parseVersion("12").value shouldEqual V(12)

    V parseVersion "" shouldBe empty
    V parseVersion "1.2.5" shouldBe empty
    V parseVersion "1.0." shouldBe empty
    V parseVersion "-1.0" shouldBe empty
    V parseVersion "1.f" shouldBe empty
    V parseVersion "really not a version" shouldBe empty
  }

  it should "pattern match versions correctly" in {
    val V = Version.ofSize(2 to 3)

    inside(V(1, 2, 5)) { case V(1, 2, 5) => }
    inside(V(0, 0, 0)) { case V(0, 0, 0) => }
    inside(V(1, 3)) { case V(1, 3) => }

    V unapplySeq Version(1) shouldBe empty
    V unapplySeq Version(1, 2, 5, 4) shouldBe empty
  }

  it should "pattern match version strings correctly" in {
    val V = Version.ofSize(1 to 2)

    inside("1.3") { case V(1, 3) => }
    inside("0.0") { case V(0, 0) => }
    inside("12") { case V(12) => }

    V unapplySeq "" shouldBe empty
    V unapplySeq "1.2.5" shouldBe empty
    V unapplySeq "1.0." shouldBe empty
    V unapplySeq "-1.0" shouldBe empty
    V unapplySeq "1.f" shouldBe empty
    V unapplySeq "really not a version" shouldBe empty
  }

  it should "convert to other types correctly" in {
    import Convert.Throwing.Implicit.ref
    val v1 = Version(1, 2, 5)

    v1 to Version.ofSize(2 to 3) shouldEqual v1
    an [IllegalArgumentException] should be thrownBy { v1 to Version.ofSize(1 to 2) }

    val v2 = Version.ofSize(1 to 2)(1)

    v2 to Version shouldEqual v2
    an [IllegalArgumentException] should be thrownBy { v2 to Version.ofSize(2 to 3) }
  }

  it should "convert as an option to other types correctly" in {
    import Convert.AsOption.Implicit.ref
    val v1 = Version(1, 2, 5)

    v1.to(Version.ofSize(2 to 3)).value shouldEqual v1
    v1 to Version.ofSize(1 to 2) shouldBe empty

    val v2 = Version.ofSize(1 to 2)(1)

    v2.to(Version).value shouldEqual v2
    v2 to Version.ofSize(2 to 3) shouldBe empty
  }
}
