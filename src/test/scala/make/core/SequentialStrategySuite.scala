package make.core

import make.MakeSuite

import scala.collection.mutable.ArrayBuffer

class SequentialStrategySuite extends MakeSuite {
  test("simple execution") {
    import DSL._

    val list = new ArrayBuffer[String]
    val rec = recipe {
      _.task("a")("b", "c") {
        _ => list += "a"
      }.task("b")() {
        _ => list += "b"
      }.task("c")() {
        _ => list += "c"
      }
    }

    val strategy = new SequentialStrategy
    strategy.execute(rec)

    assert(list(0) == "b")
    assert(list(1) == "c")
    assert(list(2) == "a")
  }

  test("nested execution") {
    import DSL._

    val list = new ArrayBuffer[String]
    val rec = recipe {
      _.task("a")("b", "c") {
        _ => list += "a"
      }.task("b")("c", "d") {
        _ => list += "b"
      }.task("c")() {
        _ => list += "c"
      }.task("d")() {
        _ => list += "d"
      }
    }

    val strategy = new SequentialStrategy
    strategy.execute(rec)

    def before(list: ArrayBuffer[String], x: String, y: String): Boolean = {
      list.indexOf(x) < list.indexOf(y) && list.indexOf(x) != -1 && list.indexOf(y) != -1
    }

    assert(before(list, "b", "a"))
    assert(before(list, "c", "a"))
    assert(before(list, "d", "a"))
    assert(before(list, "c", "b"))
    assert(before(list, "d", "b"))
  }
}
