package com.trukr.shipper.model;

/**
 * Created by HP PC on 02-04-2016.
 */
public class GetBookNowRequest {
    public String UserId, UserType, AuthToken;
    public Shipment Shipment;

    public GetBookNowRequest() {
        Shipment = new Shipment();
    }

    public class Shipment {
        public String FromLatitude = "", FromLongitude = "", FromAddress = "", ToLatitude = "", ToLongitude = "", ToAddress = "",BorderCrossStatus ="", TruckType = "", PickupTime = "", DeliveryTime = "", Notes = "";
        public Accessories Accessories;

        public Shipment() {
            Accessories = new Accessories();
        }

        public class Accessories {
            public String BorderCrossing = "", Loadstraps = "", HazardousMaterial = "", RedeliveryCharge = "", StopsinTransit = "", TeamService = "";
        }
    }
}
/*
"FromLatitude": "23452.345",
        "FromLongitude": "3454.1234",
        "FromAddress": "Canada",
        "ToLatitude": "23452.345",
        "ToLongitude": "3454.1234",
        "ToAddress": "Mexico",
        "TruckType": "123",
        "PickupTime": "DD/MM/YYYY HH:MM",
        "DeliveryTime": "DD/MM/YYYY HH:MM",
        "Notes": "shgtyhrtasrdgfvdsafgads",
        "Accessories": {
        "BorderCrossing": "2",
        "Loadstraps": "50",
        "HazardousMaterial": "0",
        "RedeliveryCharge": "0",
        "StopsinTransit": "50",
        "TeamService": "0"
        }*/
