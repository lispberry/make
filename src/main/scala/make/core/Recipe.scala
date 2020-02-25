package make.core

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class Recipe[R] {
  private val env = new mutable.HashMap[String, Task[R]]

  private[core] def addTask(task: Task[R]): Unit = {
    // TODO: error-handling
    env(task.name) = task
  }

  // TODO: error-handling
  private[core] def toTask(name: String): Task[R] = env(name)

  private[core] def topsort: List[Task[R]] = {
    val used = new mutable.HashSet[String]
    val res = new ListBuffer[Task[R]]

    def dfs(name: String): Unit = {
      if (used(name)) return

      used.add(name)
      for (dep <- toTask(name).dependencies) {
        if (!used.contains(dep)) {
          dfs(dep)
        }
      }
      res += toTask(name)
    }

    for (name <- env.keysIterator) {
      dfs(name)
    }
    res.toList
  }

  def tasks: Iterator[Task[R]] = env.valuesIterator
}
