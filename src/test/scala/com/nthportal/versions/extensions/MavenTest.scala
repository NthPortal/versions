package com.nthportal.versions
package extensions

import com.nthportal.versions.extensions.Maven._

class MavenTest extends SimpleSpec {

  behavior of "Maven extension"

  it should "compare correctly" in {
    Release should be > Snapshot
  }

  it should "produce the correct string representations for extensions" in {
    extensionDef.extToString(Snapshot) should be ("-SNAPSHOT")
    extensionDef.extToString(Release) should be ("")
  }

  it should "parse extension values correctly" in {
    parse("SNAPSHOT") should be theSameInstanceAs Snapshot
    an [IllegalArgumentException] should be thrownBy {parse("INVALID")}
    an [IllegalArgumentException] should be thrownBy {parse("")}
  }
}
