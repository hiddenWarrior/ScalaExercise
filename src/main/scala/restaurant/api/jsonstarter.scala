package restaurant.api

import spray.json.DefaultJsonProtocol._
import spray.json.JsArray
import spray.json.JsNumber
import spray.json.JsObject
import spray.json.JsString
import spray.json.JsValue
import spray.json.JsonFormat
import restaurant.data.data.{RestaurantData,Restaurant}



trait JsonMarshaller{
    implicit object OptionFormat extends JsonFormat[Option[String]] {
    def write(str: Option[String]) = JsString(str.getOrElse(""))

    def read(str: JsValue) = {
      str match {
        case JsString(s) => if (s.isEmpty) None else Some(s)
        case _ => None //throw new DeserializationException("Expected non empty string")
      }
    }
  }
    implicit val RestaurantDataFormat = jsonFormat18(RestaurantData)
    implicit val RestaurantFormat = jsonFormat2(Restaurant)

}