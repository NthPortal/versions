package com.nthportal.versions
package extensions

import com.nthportal.versions.extensions.AlphaBeta._

class AlphaBetaTest extends SimpleSpec {
  behavior of "AlphaBeta extension"

  it should "compare correctly" in {
    release should be > rc(2)
    rc(2) should be > rc(1)
    rc(1) shouldNot be > rc(1)
    rc(1) shouldNot be < rc(1)
    rc(1) should be > beta
    beta should be > alpha
    alpha should be > preAlpha
  }

  it should "only allow positive release candidate numbers" in {
    an [IllegalArgumentException] should be thrownBy {rc(-1)}
    an [IllegalArgumentException] should be thrownBy {rc(0)}
  }

  it should "produce the correct string representations for extensions" in {
    extensionDef.extToString(preAlpha) should be ("-pre-alpha")
    extensionDef.extToString(alpha) should be ("-alpha")
    extensionDef.extToString(beta) should be ("-beta")
    extensionDef.extToString(rc(1)) should be ("-rc.1")
    extensionDef.extToString(rc(2)) should be ("-rc.2")
  }

  it should "parse extension values correctly" in {
    parse("pre-alpha") should be (preAlpha)
    parse("alpha") should be (alpha)
    parse("beta") should be (beta)
    parse("rc.1") should be (rc(1))
    parse("rc.2") should be (rc(2))

    an [IllegalArgumentException] should be thrownBy {parse("invalid")}
    an [IllegalArgumentException] should be thrownBy {parse("rc1")}
    an [IllegalArgumentException] should be thrownBy {parse("rc.0")}
    an [IllegalArgumentException] should be thrownBy {parse("rc.-1")}
    an [IllegalArgumentException] should be thrownBy {parse("rc.NaN")}
    an [IllegalArgumentException] should be thrownBy {parse("")}
  }
}