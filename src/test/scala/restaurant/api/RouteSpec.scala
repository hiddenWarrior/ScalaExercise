package restaurant.api

//#user-routes-spec
//#test-top
import akka.actor.ActorRef
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ Matchers, WordSpec }
import akka.util.ByteString
import restaurant.data.Restaurant
import scala.concurrent.ExecutionContext


import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._


class RestaurantRoutesSpec extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest with RestaurantRoute {
        
    override implicit val executionContext = ExecutionContext.global

    val uuid = "5dc94cbf-add9-11e7-b988-0242ac110002"
    val uuid2 = "5dc94e49-add9-11e7-b988-0242ac110002"
    val strRequest = """
            {
        "uuid": "5dc94cbf-add9-11e7-b988-0242ac110002",
        "data": {
            "enName": "After Eight(Closed)",
            "arName": "أفتر ايت(مغلق)",
            "state": "UNPUBLISHED",
            "routingMethod": null,
            "logo": "ig22v040ud4h1tt9.jpg",
            "coverPhoto": null,
            "enDescription": "",
            "arDescription": null,
            "shortNumber": "11361",
            "facebookLink": "",
            "twitterLink": "",
            "youtubeLink": "",
            "website": null,
            "onlinePayment": false,
            "client": false,
            "pendingInfo": true,
            "pendingMenu": true,
            "closed": true
        }
    }
        """.stripMargin
    val jsonRequest = ByteString(strRequest)
    val jsonRequest2 = ByteString("""{
        "uuid": "5dc94e49-add9-11e7-b988-0242ac110002",
        "data": {
            "enName": "Spago",
            "arName": "سباجو",
            "state": "PUBLISHED",
            "routingMethod": null,
            "logo": "e9fo4fb8.jpg",
            "coverPhoto": "irotwr8uyn0l766r.jpg",
            "enDescription": "",
            "arDescription": null,
            "shortNumber": "",
            "facebookLink": "",
            "twitterLink": "",
            "youtubeLink": "",
            "website": null,
            "onlinePayment": false,
            "client": false,
            "pendingInfo": true,
            "pendingMenu": true,
            "closed": false
        }
    }""".stripMargin)
    val jsonRequest3 = ByteString(strRequest.replace("false","true"))

  "RestaurantRoutes" should {
        "return empty list if no restaurants were added" in {

            HttpRequest(uri = "/api/restaurant") ~> route ~> check {
                status should ===(StatusCodes.OK)

                // we expect the response to be json:
                contentType should ===(ContentTypes.`application/json`)

                // and no entries should be in the list:
                entityAs[String] should ===("""[]""")
            }
            clean
        }

        "getting closed and opened restaurants only (GET /api/restaurants)" in {
            
            val request1 = HttpRequest(
                HttpMethods.POST,
                uri = "/api/restaurant",
                entity = HttpEntity(MediaTypes.`application/json`, jsonRequest))

            val request2 = HttpRequest(
                HttpMethods.POST,
                uri = "/api/restaurant",
                entity = HttpEntity(MediaTypes.`application/json`, jsonRequest2))

            request1 ~> route
            request2 ~> route
            HttpRequest(uri = "/api/restaurant?closed=0") ~> route ~> check{
                entityAs[ List[Restaurant] ].length should ===(1)
            }
            HttpRequest(uri = "/api/restaurant?closed=1") ~> route ~> check{
                entityAs[ List[Restaurant] ].length should ===(1)
            }

            HttpRequest(uri = "/api/restaurant") ~> route ~> check{
                entityAs[ List[Restaurant] ].length should ===(2)
            }
            clean

        }

        "be able to add restataurants (POST /api/restaurants)" in {
            
            val request = HttpRequest(
                HttpMethods.POST,
                uri = "/api/restaurant",
                entity = HttpEntity(MediaTypes.`application/json`, jsonRequest))

            request ~> route ~> check {
                    // and we know what message we're expecting back:
                    entityAs[String] should ===("""done""")
            }
            HttpRequest(uri = "/api/restaurant") ~> route ~> check {
                status should ===(StatusCodes.OK)

                // we expect the response to be json:
                contentType should ===(ContentTypes.`application/json`)
                entityAs[ List[Restaurant] ].head.uuid should ===(uuid)
            }
            clean
        }
        "prevent adding duplicate restaurants (POST /api/restaurants)" in {
            
            val request = HttpRequest(
                HttpMethods.POST,
                uri = "/api/restaurant",
                entity = HttpEntity(MediaTypes.`application/json`, jsonRequest))

            request ~> route
            request ~> route ~> check {
                status should ===(StatusCodes.AlreadyReported)
            }
            
            HttpRequest(uri = "/api/restaurant") ~> route ~> check {
                status should ===(StatusCodes.OK)

                // we expect the response to be json:
                contentType should ===(ContentTypes.`application/json`)
                entityAs[ List[Restaurant] ].length should ===(1)
            }
            clean


        }

        "updating (PUT /api/restaurants/uuid)" in {
            
            val addRequest = HttpRequest(
                HttpMethods.POST,
                uri = "/api/restaurant",
                entity = HttpEntity(MediaTypes.`application/json`, jsonRequest))

            addRequest ~> route
            val updateRequest = HttpRequest(
                HttpMethods.PUT,
                uri = "/api/restaurant/"+uuid,
                entity = HttpEntity(MediaTypes.`application/json`, jsonRequest3))

            updateRequest ~> route ~> check {
                 status should ===(StatusCodes.OK)
            }

            HttpRequest(uri = "/api/restaurant") ~> route ~> check {
                status should ===(StatusCodes.OK)

                // we expect the response to be json:
                contentType should ===(ContentTypes.`application/json`)
                entityAs[ List[Restaurant] ].head.data.onlinePayment should ===(true)
            }
            clean


        }

        "prevent request from updating not found object (PUT /api/restaurants/uuid)" in {
            
            val addRequest = HttpRequest(
                HttpMethods.POST,
                uri = "/api/restaurant",
                entity = HttpEntity(MediaTypes.`application/json`, jsonRequest))

            addRequest ~> route
            val updateRequest = HttpRequest(
                HttpMethods.PUT,
                uri = "/api/restaurant/"+uuid2,
                entity = HttpEntity(MediaTypes.`application/json`, jsonRequest2))
            updateRequest ~> route ~> check {
                 status should !==(StatusCodes.OK)
            }
            clean

        }
 


    }
}
