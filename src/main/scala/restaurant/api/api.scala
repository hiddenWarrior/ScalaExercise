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

import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import restaurant.data.data.{RestaurantData,Restaurant}


// private case class RestaurantList(rests:List[Restaurant]){
//     def append_if_exists(restaurant){
//         restaurants.find(_.uuid == restaurant.uuid) match{
//                     case Some(restaurant) => rests,false
//                     case None => 
//                     restaurants = rest :: restaurants,true
//                     complete("done") 
//         }

//     }
// }

object main extends JsonMarshaller with App{

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher
    var restaurants = List[Restaurant]()
        
    val route =
      redirectToNoTrailingSlashIfPresent(StatusCodes.MovedPermanently) {
        pathPrefix("api" / "restaurant") {
        
          get {
            parameters('closed.as[Boolean]) { closed =>
              complete(restaurants.filter(_.data.closed == closed))
              } ~
              pathEnd{
                complete(restaurants)
              }
            
          } ~
          post {
               entity(as[Restaurant]){ rest =>
                restaurants.find(_.uuid == rest.uuid) match{
                    case Some(restaurant) => complete(StatusCodes.AlreadyReported)
                    case None => 
                    restaurants = rest :: restaurants
                    complete("done") 
                }
                
              }
          } ~
          put{
            path(JavaUUID){ uuid =>
              entity(as[Restaurant]){ rest =>
                restaurants.find(_.uuid == uuid) match{
                    case Some(restaurant) => 
                        new_restaurants = ()
                    
                    case None => 
                    restaurants = rest :: restaurants
                    complete("done")
                }
              }
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