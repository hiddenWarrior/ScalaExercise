package restaurant.data.data

case class RestaurantData(enName: String,arName: String,state: String,
routingMethod: Option[String],logo :Option[String],coverPhoto: Option[String],enDescription: Option[String],
arDescription: Option[String],shortNumber: String,facebookLink: String,twitterLink: Option[String],
youtubeLink: Option[String],website: Option[String],onlinePayment: Boolean,client: Boolean,
pendingInfo: Boolean,pendingMenu: Boolean,closed: Boolean)

case class Restaurant(uuid:String,data:RestaurantData)

