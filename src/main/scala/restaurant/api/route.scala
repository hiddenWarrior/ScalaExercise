package restaurant.api

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes

import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import scala.util.{ Failure, Success }
import scala.concurrent.{Future, ExecutionContext}

import restaurant.data.{RestaurantData,Restaurant}





case class RestaurantList(rests:List[Restaurant]){


    def append_if_not_exists(restaurant: Restaurant):RestaurantList =  {
        this.rests.find(_.uuid == restaurant.uuid) match{
                    case Some(restaurant) => this//RestaurantList(rests)
                    case None => RestaurantList(restaurant :: rests)
        }

    }

    def update_if_exists(uuid: String,restaurant: Restaurant): Tuple2[RestaurantList,Boolean] = {
        this.rests.find(_.uuid == uuid) match{
                    case Some(r) => 
                    (RestaurantList(rests.map{
                        case Restaurant(uuid,_) => restaurant
                        case a => a   }),true)
                    case None => (this,false)//RestaurantList(rests)
        }
    }
    
}

trait RestaurantRoute extends JsonMarshaller{

 
    var restaurants = RestaurantList(List[Restaurant]())
    implicit val executionContext = ExecutionContext.global
    lazy val route =
      redirectToNoTrailingSlashIfPresent(StatusCodes.MovedPermanently) {
        pathPrefix("api" / "restaurant") {
        
          get {
              parameters('closed.as[Boolean]) { closed =>
              
              val filteredRestaurant = Future {
                restaurants.rests.filter(_.data.closed == closed)
              }

              onSuccess(filteredRestaurant){ rests =>
                  complete(rests)
                  
              }
              } ~
              pathEnd{
                complete(restaurants.rests)
              }
            
          } ~
          post {
               entity(as[Restaurant]){ rest =>
               //i'm not gonna use future because i used var so thread safety
                val new_restaurants = restaurants.append_if_not_exists(rest)
                if(restaurants != new_restaurants){ 
                    restaurants = new_restaurants
                    complete("done")
                }
                else
                complete(StatusCodes.AlreadyReported)

                
              }
          } ~
          put{
            path(JavaUUID){ uuid =>
              entity(as[Restaurant]){ rest =>
              //i'm not gonna use future because i used var so thread safety
              val (new_restaurants,updated) = restaurants.update_if_exists(uuid.toString,rest)
                if(updated) {
                    restaurants = new_restaurants
                    complete("done")
                }
                else{
                    complete(StatusCodes.custom(409, "object not found", s"object with uuid $uuid not found"))
                }
            }
            }
          }

        }
      }



}