package com.trukr.shipper.model;

/**
 * Created by kalaivani on 3/27/2016.
 */
public class AddCardModel {

public String CardTypeId,CardNumber,NameOnCard,IsDefault,Expiry,UserId,UserType
        ,AuthToken;

    public ExpiryOn ExpiryOn;
    public AddCardModel(){
        ExpiryOn = new ExpiryOn();
    }
   /*

   "UserId": "176",
  "UserType": "1",
  "AuthToken": "86c367f50a",

   "UserId": "176",
            "UserType": "1",
            "AuthToken": "86c367f50a",
            "CardTypeId": "1",
            "CardNumber": "f2f7dbc4e71ca1c2fb52fbc287f8e1e1",
            "NameOnCard": "joshua",
            "ExpiryOn": {
        "MM": "02",
                "YY": "24"
    },
            "IsDefault": "0"*/


    public class ExpiryOn{

        public String MM,YY;
        public ExpiryOn(){
            MM="";
            YY="";
        }
    }
}
