package com.nthportal.versions
package extensions

import com.nthportal.versions.extensions.Maven._

class MavenTest extends SimpleSpec {

  behavior of "Maven extension"

  it should "compare correctly" in {
    Release should be > Snapshot
  }

  it should "have the correct string values for extensions" in {
    Snapshot.toString should be ("SNAPSHOT")

    Maven.parse("SNAPSHOT") should be theSameInstanceAs Snapshot
    an [IllegalArgumentException] should be thrownBy {Maven.parse("INVALID")}
  }

  it should "produce the correct string representation" in {
    extensionDef.extToString(Snapshot) should be ("-SNAPSHOT")
    extensionDef.extToString(Release) should be ("")
  }
}
