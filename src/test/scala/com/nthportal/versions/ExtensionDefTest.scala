package com.nthportal.versions

import com.nthportal.versions.ExtensionDef._
import com.nthportal.versions.extensions.Maven
import com.nthportal.versions.extensions.Maven._

class ExtensionDefTest extends SimpleSpec {

  behavior of "ExtensionDef"

  it should "evaluate the correct string representation of an extension" in {
    fromOrdered[Maven] extToString Snapshot shouldBe s"-$Snapshot"
    fromOrdered[Maven](Release) extToString Snapshot shouldBe s"-$Snapshot"
    fromOrdered[Maven](Release) extToString Release shouldBe empty
  }

  it should "create definitions with the correct ordering" in {
    fromOrdered[Maven].compare(Release, Snapshot) should equal (Release compare Snapshot)
    fromOrdered[Maven].compare(Snapshot, Release) should equal (Snapshot compare Release)
    fromOrdered[Maven].compare(Release, Release) should equal (Release compare Release)

    fromOrdered[Maven](Snapshot).compare(Release, Snapshot) should equal (Release compare Snapshot)
    fromOrdered[Maven](Snapshot).compare(Snapshot, Release) should equal (Snapshot compare Release)
    fromOrdered[Maven](Snapshot).compare(Release, Release) should equal (Release compare Release)

    fromComparable[Maven].compare(Release, Snapshot) should equal (Release compare Snapshot)
    fromComparable[Maven].compare(Snapshot, Release) should equal (Snapshot compare Release)
    fromComparable[Maven].compare(Release, Release) should equal (Release compare Release)

    fromComparable[Maven](Snapshot).compare(Release, Snapshot) should equal (Release compare Snapshot)
    fromComparable[Maven](Snapshot).compare(Snapshot, Release) should equal (Snapshot compare Release)
    fromComparable[Maven](Snapshot).compare(Release, Release) should equal (Release compare Release)
  }
}
