package make.core

class SequentialStrategy extends ExecutionStrategy {
  override def execute[R](recipe: Recipe[R]): Unit = {
    val tasks = recipe.topsort

    for (task <- tasks) {
      if (task.outOfDate) {
        task.execute()
      }
    }
  }
}