package restaurant.data.data

case class RestaurantData(enName: String,arName: String,state: String,
routingMethod: Option[String],logo :String,coverPhoto: String,enDescription: String,
arDescription: String,shortNumber: String,facebookLink: String,twitterLink: String,
youtubeLink: String,website: String,onlinePayment: Boolean,client: Boolean,
pendingInfo: Boolean,pendingMenu: Boolean,closed: Boolean)

case class Restaurant(uuid:String,data:RestaurantData)

