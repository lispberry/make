package make.core

class SequentialStrategy extends ExecutionStrategy {
  override def execute(recipe: Recipe): Unit = {
    val tasks = recipe.topsort

    for (task <- tasks) {
      if (task.outOfDate) {
        task.execute()
      }
    }
  }
}