package restaurant.api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn
import akka.http.scaladsl.model.StatusCodes

import scala.concurrent.{ Await, ExecutionContext, Future }
import scala.concurrent.duration.Duration
import scala.util.{ Failure, Success }



object main extends App {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher
    val route =
      redirectToNoTrailingSlashIfPresent(StatusCodes.MovedPermanently) {
        pathPrefix("api" / "restaurant") {
        
          get {
            parameters('closed.as[Boolean]) { closed =>
              val response = if(closed) "closed restaurants" else "opened restaurants"
              complete(response)
              } ~
              pathEnd{
                complete("all restaurants")
              }
            
          } ~
          post {
               complete("post to all restaurants")
          } ~
          put{
            path(JavaUUID){ uuid =>
              complete(s"id is $uuid")
  
            }
          }

        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

      bindingFuture.onComplete {
    case Success(bound) =>
      println(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
    case Failure(e) =>
      Console.err.println(s"Server could not start!")
      e.printStackTrace()
      system.terminate()
  }

  Await.result(system.whenTerminated, Duration.Inf)
}