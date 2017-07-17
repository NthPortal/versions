package com.nthportal.versions
package variable

import com.nthportal.versions.extensions.Maven._

class ExtractorTest extends SimpleSpec {
  behavior of "-- extractor (variable)"

  it should "pattern match versions correctly" in {
    inside(Version(1, 2, 5, 4, 16) -- Snapshot) { case Version(1, 2, 5, 4, 16) -- Snapshot => }
  }
}
