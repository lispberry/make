package make.core

class PhonyTask(owner: Recipe, name: String, command: Command) extends Task(owner, name, command) {
  override def timestamp: Long = Long.MaxValue

  override def outOfDate: Boolean = true
}
