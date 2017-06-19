package com.nthportal.versions
package v2

import com.nthportal.versions.extensions.Maven._

class ExtractorTest extends SimpleSpec {

  behavior of "-- extractor (2)"

  it should "pattern match versions correctly" in {
    inside(V(1, 3) -- Snapshot) { case V(1, 3) -- Snapshot => }
  }

}
