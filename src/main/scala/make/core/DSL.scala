package make.core

class DSL[R](val owner: Recipe[R]) {
  def file[R](name: String)(deps: String*)(command: Command[R]): DSL[R] = {
    val task = new FileTask[R](owner, name, command)
    for (dep <- deps) {
      task.addDependency(dep)
    }

    this
  }

  def recipe: Recipe[R] = owner
}

object DSL {
  def recipe[R](fn: DSL[R] => DSL[R]): Recipe[R] = {
    fn(new DSL(new Recipe[R])).recipe
  }
}