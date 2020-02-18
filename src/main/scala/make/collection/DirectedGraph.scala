package make.collection

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class DirectedGraph[V] {
  type Map = mutable.HashMap[V, mutable.ArrayBuffer[V]]
  private val map = new Map()

  private def addVertex(v: V): Unit = {
    if (!map.contains(v)) {
      map(v) = new mutable.ArrayBuffer[V]
    }
  }

  def addEdge(x: V, y: V): Unit = {
    addVertex(x)
    addVertex(y)

    map(x) += y
  }

  def edges(owner: V): Iterator[V] = {
    addVertex(owner)
    for (v <- map(owner).iterator) yield v
  }

  def topsort(): List[V] = {
    def dfs(fn: V => Unit): Unit = {
      val used = new mutable.HashSet[V]()

      def loop(v: V): Unit = {
        if (used.contains(v)) return;

        used(v) = true
        for (out <- map(v)) {
          if (!used.contains(out)) {
            loop(out)
          }
        }
        fn(v)
      }

      for (v <- map.keysIterator) {
        loop(v)
      }
    }

    val res = new ListBuffer[V]
    dfs(v => res += v)
    res.toList
  }
}