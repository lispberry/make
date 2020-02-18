package make.collection

import make.MakeSuite

class DirectedGraphSuite extends MakeSuite {
  test("topsort") {
    def before(list: List[Int], x: Int, y: Int): Boolean = {
      list.indexOf(x) < list.indexOf(y) && list.indexOf(x) != -1 && list.indexOf(y) != -1
    }

    val graph = new DirectedGraph[Int]
    graph.addEdge(1, 2)
    graph.addEdge(2, 3)
    graph.addEdge(3, 4)
    graph.addEdge(3, 5)

    val list = graph.topsort()
    assert(before(list, 2, 1))
    assert(before(list, 3, 2))
    assert(before(list, 3, 1))
    assert(before(list, 4, 3))
    assert(before(list, 5, 3))
  }
}