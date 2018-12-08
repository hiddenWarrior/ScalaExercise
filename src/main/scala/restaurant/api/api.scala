package restaurant.api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
// import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn
// import akka.http.scaladsl.model.StatusCodes

import scala.concurrent.{ Await, ExecutionContext, Future }
import scala.concurrent.duration.Duration
import scala.util.{ Failure, Success }

// import spray.json.DefaultJsonProtocol._
// import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
// import restaurant.data.data.{RestaurantData,Restaurant}


// case class RestaurantList(rests:List[Restaurant]){
//     def append_if_not_exists(restaurant: Restaurant):RestaurantList =  {
//         this.rests.find(_.uuid == restaurant.uuid) match{
//                     case Some(restaurant) => this//RestaurantList(rests)
//                     case None => RestaurantList(restaurant :: rests)
//         }

//     }

//     def update_if_exists(uuid: String,restaurant: Restaurant): Tuple2[RestaurantList,Boolean] = {
//         this.rests.find(_.uuid == uuid) match{
//                     case Some(restaurant) => 
//                     (RestaurantList(rests.map{
//                         case Restaurant(restaurant.uuid,_) => restaurant
//                         case a => a   }),true)
//                     case None => (this,false)//RestaurantList(rests)
//         }
//     }
    
// }

object main extends RestaurantRoute with App{

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher
    // var restaurants = List[Restaurant]()
    // var restaurants = RestaurantList(List[Restaurant]())
        
    // lazy val route =
    //   redirectToNoTrailingSlashIfPresent(StatusCodes.MovedPermanently) {
    //     pathPrefix("api" / "restaurant") {
        
    //       get {
    //         parameters('closed.as[Boolean]) { closed =>
    //           complete(restaurants.rests.filter(_.data.closed == closed))
    //           } ~
    //           pathEnd{
    //             complete(restaurants.rests)
    //           }
            
    //       } ~
    //       post {
    //            entity(as[Restaurant]){ rest =>
    //             // restaurants.find(_.uuid == rest.uuid) match{
    //             //     case Some(restaurant) => complete(StatusCodes.AlreadyReported)
    //             //     case None => 
    //             //     restaurants = rest :: restaurants
    //             //     complete("done") 
    //             // }
    //             val new_restaurants = restaurants.append_if_not_exists(rest)
    //             if(restaurants != new_restaurants){ 
    //                 restaurants = new_restaurants
    //                 complete("done")
    //             }
    //             else
    //             complete(StatusCodes.AlreadyReported)

                
    //           }
    //       } ~
    //       put{
    //         path(JavaUUID){ uuid =>
    //           entity(as[Restaurant]){ rest =>
    //           val (new_restaurants,updated) = restaurants.update_if_exists(uuid.toString,rest)
    //             if(updated) {
    //                 restaurants = new_restaurants
    //                 complete("done")
    //             }
    //             else{
    //                 complete(StatusCodes.custom(409, "object not found", s"object with uuid $uuid not found"))
    //             }
    //             // restaurants.find(_.uuid == uuid) match{
    //             //     case Some(restaurant) => 
    //             //         new_restaurants = ()
                    
    //             //     case None => 
    //             //     restaurants = rest :: restaurants
    //             //     complete("done")
    //             // }
    //           }
    //         }
    //       }

    //     }
    //   }

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