package make.core

class Engine(var strategy: ExecutionStrategy = new SequentialStrategy) {
  def execute(recipe: Recipe): Unit = {
    strategy.execute(recipe)
  }
}
