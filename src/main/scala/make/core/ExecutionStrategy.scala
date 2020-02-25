package make.core

trait ExecutionStrategy {
  def execute(recipe: Recipe): Unit
}
