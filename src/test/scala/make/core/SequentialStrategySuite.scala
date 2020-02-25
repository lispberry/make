package make.core

import java.nio.file.Files

import make.MakeSuite

import scala.collection.mutable.ArrayBuffer

class SequentialStrategySuite extends MakeSuite {
  import DSL._

  test("simple execution") {
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

  test("FileTask timestamp is Long.MaxValue if the file doesn't exist") {
    val file = recipe {
      _.file("__special__")() { _ => () }
    }.toTask("__special__")

    assert(file.timestamp == Long.MaxValue)
  }

  test("FileTask is executed if the file doesn't exist and it has no dependencies") {
    var called = false

    val rec = recipe {
      _.file("__special__")() { _ => called = true }
    }

    val strategy = new SequentialStrategy
    strategy.execute(rec)

    assert(called)
  }

  test("FileTask isn't executed if its dependencies were modified earlier than the target itself") {
    var called = false

    val file1 = Files.createTempFile("__", ".test")
    val file2 = Files.createTempFile("__", ".test")

    val rec = recipe {
      _.file(file2.toAbsolutePath.toString)(file1.toAbsolutePath.toString) {
        _ => called = true
      }.file(file1.toAbsolutePath.toString)() {
        _ => ()
      }
    }

    val strategy = new SequentialStrategy
    strategy.execute(rec)

    assert(!called)
  }

  test("FileTask is always executed if it has a Phony dependency") {
    var called = false

    val file1 = Files.createTempFile("__", ".test")
    val file2 = Files.createTempFile("__", ".test")

    val rec = recipe {
      _.file(file2.toAbsolutePath.toString)("b", file1.toAbsolutePath.toString) {
        _ => called = true
      }.file(file1.toAbsolutePath.toString)() {
        _ => ()
      }.task("b")() {
        _ => ()
      }
    }

    val strategy = new SequentialStrategy
    strategy.execute(rec)

    assert(called)
  }
}
