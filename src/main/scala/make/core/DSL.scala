package make.core

import sys.process._

class DSL(val owner: Recipe) {
  def file(name: String)(deps: String*)(command: Command): DSL = {
    val task = new FileTask(owner, name, command)
    for (dep <- deps) {
      // TODO: either make sure nonexistent tasks are permitted or create a specific Task subclass.
      // TODO: UGLY, UGLY, UGLY
      if (!owner.hasTask(dep)) {
        val task = new PhonyTask(owner, dep, { _ => () })
        owner.addTask(task)
      }

      task.addDependency(dep)
    }
    owner.addTask(task)

    this
  }

  def task(name: String)(deps: String*)(command: Command): DSL = {
    // TODO: copy-paste
    val task = new PhonyTask(owner, name, command)
    for (dep <- deps) {
      // TODO: either make sure nonexistent tasks are permitted or create a specific Task subclass.
      // TODO: UGLY, UGLY, UGLY
      if (!owner.hasTask(dep)) {
        val task = new PhonyTask(owner, dep, { _ => () })
        owner.addTask(task)
      }

      task.addDependency(dep)
    }
    owner.addTask(task)

    this
  }

  def recipe: Recipe = owner
}

object DSL {
  def sh(cmd: String): String = {
    cmd.!!
  }

  def recipe(fn: DSL => DSL): Recipe = {
    fn(new DSL(new Recipe)).recipe
  }
}