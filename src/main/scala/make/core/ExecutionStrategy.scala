package make.core

trait ExecutionStrategy {
  def execute[R](recipe: Recipe[R]): Unit
}
