package make.core

import make.MakeSuite

class DSLSuite extends MakeSuite {
  test("FileTask creation") {
    import DSL._

    val rec = recipe {
      _.file("a")( "b", "c") {
        x => x
      }.file("c")("d", "e") {
        x => x
      }
    }

    assert(rec.hasTask("a"))
    assert(rec.toTask("a").name == "a")
    assert(rec.hasTask("b"))
    assert(rec.toTask("b").name == "b")
    assert(rec.hasTask("c"))
    assert(rec.toTask("c").name == "c")
    assert(rec.hasTask("d"))
    assert(rec.toTask("d").name == "d")
    assert(rec.hasTask("e"))
    assert(rec.toTask("e").name == "e")

    // TODO: inefficient?
    val adeps = rec.toTask("a").dependencies.toList
    assert(adeps.contains("b"))
    assert(adeps.contains("c"))
    val cdeps = rec.toTask("c").dependencies.toList
    assert(cdeps.contains("d"))
    assert(cdeps.contains("e"))

    assert(rec.toTask("b").dependencies.isEmpty)
    assert(rec.toTask("d").dependencies.isEmpty)
  }
}
