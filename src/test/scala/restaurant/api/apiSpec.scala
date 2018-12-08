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


class RestaurantRoutesSpec extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest
    with RestaurantRoute {
    val jsonRequest = ByteString(
        """
        {
        "uuid": "5d81a479-add9-11e7-b988-0242ac10002",
        "data": {
            "enName": "3 Ahwa Cafe",
            "arName": "عالقهوة كافيه",
            "state": "PUBLISHED",
            "routingMethod": null,
            "logo": "i3qf6gym1p833di.jpg",
            "coverPhoto": "",
            "enDescription": "",
            "arDescription": "",
            "shortNumber": "",
            "facebookLink": "",
            "twitterLink": "",
            "youtubeLink": "",
            "website": "",
            "onlinePayment": true,
            "client": false,
            "pendingInfo": true,
            "pendingMenu": true,
            "closed": false
            }
        }
        """.stripMargin)
 

  "RestaurantRoutes" should {
        "return empty list if no restaurants were added" in {

            HttpRequest(uri = "/api/restaurant") ~> route ~> check {
                status should ===(StatusCodes.OK)

                // we expect the response to be json:
                contentType should ===(ContentTypes.`application/json`)

                // and no entries should be in the list:
                entityAs[String] should ===("""[]""")
            }
        }

        "be able to add restataurants (POST /api/restaurants)" in {
            
            val request = HttpRequest(
                HttpMethods.POST,
                uri = "/api/restaurants",
                entity = HttpEntity(MediaTypes.`application/json`, jsonRequest))

            request ~> route ~> check {
                    // and we know what message we're expecting back:
                    entityAs[String] should ===("""done""")
            }
        }
    }
}
