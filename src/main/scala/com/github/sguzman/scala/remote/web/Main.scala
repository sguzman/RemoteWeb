package com.github.sguzman.scala.remote.web

import lol.http._
import org.apache.commons.lang3.StringUtils

import scala.concurrent.ExecutionContext.Implicits.global
import scalaj.http.{Http, HttpResponse}

object Main {
  def main(args: Array[String]): Unit = {
    val port = System.getenv("PORT")

    Server.listen(8888)(handle)
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

  def request(e: Request): HttpResponse[String] = {
    val remoteWebHeaders = e.headers
      .filter(_._1.str.startsWith("Remote-Web-"))
      .map(t => (StringUtils.substringAfter(t._1.str, "Remote-Web-"), t._2.str))

    val scheme = e.headers.getOrElse(HttpString("Remote-Scheme"), "https")
    val method = e.method

    val host = e.headers(HttpString("Remote-Web-Host"))
    val path = e.url

    val uri = s"$scheme://$host$path"
    println(s"Received a request for $uri")

    val request = Http(uri)
      .headers(remoteWebHeaders)
      .method(method.toString)

    val requestReady = request

    val response = requestReady.asString
    response
  }
}
