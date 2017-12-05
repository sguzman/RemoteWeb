package com.github.sguzman.scala.remote.web

import lol.http.{HttpString, _}
import org.apache.commons.lang3.StringUtils

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scalaj.http.Http

object Main {
  def main(args: Array[String]): Unit = {
    val portEnv = System.getenv("PORT")
    val port = util.Try(portEnv.toInt) match {
      case Success(v) => v
      case Failure(e) =>
        Console.err.println(s"Could not find PORT env var: ${e.getMessage}")
        8888
    }

    Server.listen(port)(handle)
  }

  def handle(e: Request) = {
    try {
      val response = request(e)
      Ok(response.body)
        .addHeaders(
          (HttpString("Access-Control-Allow-Origin"), HttpString("*")),
          (HttpString("Access-Control-Allow-Headers"), HttpString("Origin, X-Requested-With, Content-Type, Accept"))
        )
    } catch {
      case e: Throwable =>
        Console.err.println(e.getMessage)
        NotFound(e.getMessage)
    }
  }

  def request(e: Request): scalaj.http.HttpResponse[String] = {
    val remoteWebHeaders = e.headers
      .filter(_._1.str.startsWith("Remote-Web-"))
      .map(t => (StringUtils.substringAfter(t._1.str, "Remote-Web-"), t._2.str))
    remoteWebHeaders foreach println

    val scheme = e.headers.getOrElse(HttpString("Remote-Scheme"), "https")
    val method = e.method

    val host = e.headers.getOrElse(HttpString("Remote-Web-Host"),HttpString("localhost"))
    val path = e.url
    val port = 8888

    val uri = s"$scheme://$host:$port$path"
    println(s"Received a request for $uri")

    val request = Http(uri)
      .headers(remoteWebHeaders)
      .method(method.toString)

    val requestReady = request

    val response = requestReady.asString
    response
  }
}
