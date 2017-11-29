package com.github.sguzman.scala.remote.web

import scala.concurrent.ExecutionContext.Implicits.global
import lol.http._

object Main {
  def main(args: Array[String]): Unit = {
    val port = System.getenv("PORT")

    Server.listen(8888)(handle)
  }

  def handle(e: Request) = {
    Ok("good")
  }
}
