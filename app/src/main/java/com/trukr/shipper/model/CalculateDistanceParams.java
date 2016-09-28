package com.trukr.shipper.model;

/**
 * Created by kalaivani on 3/26/2016.
 */
public class CalculateDistanceParams {

    public String AuthToken, UserId, UserType, OrderId, FromLat,
            FromLong, ToLat, ToLong, TruckType, FromAddress, ToAddress, PickupDate, PickupTime, DeliveryDate, DeliveryTime;
    public Accessories Accessories;

    public CalculateDistanceParams() {
        Accessories = new Accessories();
    }

    public class Accessories {
        public String BorderCrossing = "", NoOfStraps = "", StopsinTransit = "", HazardousMaterial = "", TeamService = "", RedeliveryCharge = "";
    }

   /* {
        "AuthToken": "acsdf23",
            "UserId": "123",
            "UserType": "1",
            "FromLat": "xxxx",
            "FromLong": "xxxx",
            "ToLat": "xxxx",
            "ToLong": "xxxx",
            "TruckType": "1",
            "FromAddress": "TX",
            "ToAddress": "AL-N"
    }*/
}
/*"PickupDate": "DD/MM/YYYY",
  "PickupTime": "HH:MM",
  "DeliveryDate": "DD/MM/YYYY",
  "DeliveryTime": "HH:MM",
  "Accessories": {
    "BorderCrossing": "2",
    "NoOfStraps": "50",
    "StopsinTransit": "50",
    "HazardousMaterial": 0,
    "TeamService": 1,
    "RedeliveryCharge": 1
  }*/