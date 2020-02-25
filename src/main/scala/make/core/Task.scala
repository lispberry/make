package make.core

import java.util.Date

import scala.collection.mutable.ArrayBuffer

private[core] abstract class Task[R](val owner: Recipe[R],
                                      val name: String,
                                      val command: Command[R]) {
  private val _dependencies = new ArrayBuffer[String]

  def dependencies: Iterator[String] = _dependencies.iterator
  def addDependency(name: String): Unit = _dependencies += name

  def timestamp: Long = {
    new Date().getTime / 1000L
  }

  def outOfDate: Boolean
  def execute(): Unit = {
    command(())
  }
}