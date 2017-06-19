package com.nthportal.versions
package v3

import com.nthportal.versions.extensions.Maven._

class ExtractorTest extends SimpleSpec {

  behavior of "-- extractor (3)"

  it should "pattern match versions correctly" in {
    inside(V(1, 2, 5) -- Snapshot) { case V(1, 2, 5) -- Snapshot => }
  }

}
