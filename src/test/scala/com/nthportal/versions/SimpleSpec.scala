package com.nthportal.versions

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{Inside, OptionValues}

abstract class SimpleSpec extends AnyFlatSpec with Matchers with Inside with OptionValues
