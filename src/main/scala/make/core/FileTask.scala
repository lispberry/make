package make.core

import java.io.File

class FileTask(owner: Recipe, name: String, command: Command) extends Task(owner, name, command) {
  override def timestamp: Long = {
    val file = new File(name)
    if (file.exists()) file.lastModified() else Long.MaxValue
  }

  override def outOfDate: Boolean = {
    if (dependencies.isEmpty && timestamp == Long.MaxValue) return true

    val stamp = timestamp
    dependencies.exists(x => owner.toTask(x).timestamp > stamp)
  }
}
