package make.collection

import java.util

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class DirectedGraph[V] {
  // TODO: make it thread-safe
  type Map = util.IdentityHashMap[V, mutable.ArrayBuffer[V]]
  private val map = new Map()

  private def addVertex(v: V): Unit = {
    // FIXME: we might need to make it
    if (!map.containsKey(v)) {
      map.put(v, new mutable.ArrayBuffer[V])
    }
  }

  def addEdge(x: V, y: V): Unit = {
    addVertex(x)
    addVertex(y)

    map.get(x) += y
  }

  def edges(owner: V): Iterator[V] = {
    addVertex(owner)
    for (v <- map.get(owner).iterator) yield v
  }

  def topsort(): List[V] = {
    // TODO: implement it efficiently
    def dfs(fn: V => Unit): Unit = {
      val used = new mutable.HashSet[V]()

      def loop(v: V): Unit = {
        if (used.contains(v)) return;

        used(v) = true
        for (out <- map.get(v)) {
          if (!used.contains(out)) {
            loop(out)
          }
        }
        fn(v)
      }

      map.keySet().forEach(loop(_))
    }

    val res = new ListBuffer[V]
    dfs(v => res += v)
    res.toList
  }
}