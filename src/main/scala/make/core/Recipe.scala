package make.core

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class Recipe {
  private val env = new mutable.HashMap[String, Task]

  private[core] def addTask(task: Task): Unit = {
    // TODO: error-handling
    env(task.name) = task
  }

  // TODO: error-handling
  private[core] def hasTask(name: String): Boolean = env.contains(name)
  private[core] def toTask(name: String): Task = env(name)

  private[core] def topsort: List[Task] = {
    val used = new mutable.HashSet[String]
    val res = new ListBuffer[Task]

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

  def tasks: Iterator[Task] = env.valuesIterator
}
