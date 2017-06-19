package com.nthportal.versions
package v4

import com.nthportal.versions.extensions.Maven._

class ExtractorTest extends SimpleSpec {

  behavior of "-- extractor (4)"

  it should "pattern match versions correctly" in {
    inside(V(1, 2, 5, 4) -- Snapshot) { case V(1, 2, 5, 4) -- Snapshot => }
  }

}
