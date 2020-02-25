package make.core

import make.MakeSuite

class SequentialStrategySuite extends MakeSuite {
  test("execution") {
    import DSL._

    // TODO: terrible test
    var nth = 0
    val rec = recipe {
      _.file("a")("b", "c") {
        _ => assert(nth < 2); nth += 1
      }.file("c")("b", "d") {
        _ => assert(nth < 2); nth += 1
      }.file("b")() {
        _ => assert(nth >= 2); nth += 1
      }.file("c")() {
        _ => assert(nth >= 2); nth += 1
      }.file("d")() {
        _ => assert(nth >= 2); nth += 1
      }
    }

    val strategy = new SequentialStrategy
    strategy.execute(rec)
  }
}
