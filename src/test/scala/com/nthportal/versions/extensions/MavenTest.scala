package com.nthportal.versions
package extensions

import com.nthportal.convert.Convert
import com.nthportal.versions.extensions.Maven._

class MavenTest extends SimpleSpec {

  behavior of "Maven extension"

  it should "compare correctly" in {
    Release should be > Snapshot
  }

  it should "produce the correct string representations for extensions" in {
    extensionDef extToString Snapshot shouldBe "-SNAPSHOT"
    extensionDef extToString Release shouldBe empty
  }

  it should "parse extension values correctly" in {
    import Convert.Valid.Implicit.ref

    parse("SNAPSHOT") should be theSameInstanceAs Snapshot
    an [IllegalArgumentException] should be thrownBy {parse("INVALID")}
    an [IllegalArgumentException] should be thrownBy {parse("")}
  }
}
