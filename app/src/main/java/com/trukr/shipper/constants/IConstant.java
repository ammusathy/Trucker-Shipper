package com.trukr.shipper.constants;

import com.trukr.shipper.BuildConfig;

import java.io.File;

public interface IConstant {

    String DeviceType = "2";
    String UserType = "1";
    String IsDefault = "1";
    int DialCount = 6;
    String alert = "Alert";
    String logout = "Logout";
    String delete = "Delete";
    String message = "message";
    String success = "Success";
    int StatusCode = 1;
    String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    String _EMAIL_PATTERN = "^[A-Za-z0-9,!#\\$%&'\\*\\+/=\\?\\^_`\\{\\|}~-]+(\\.[A-Za-z0-9,!#\\$%&'\\*\\+/=\\?\\^_`\\{\\|}~-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.([A-Za-z]{2,})$";
    String _EXPIRY_DATE = "(?:0[1-9]|1[0-2])/[0-9]{4}";
    String _CVV_PATTERN = "^[0-9]{3,4}$";
    String _ERR_NO_INTERNET_CONNECTION = "Please check your internet connection";
    String PAYMENT_ALERT = "Please choose a default card";
    String _ERR_SOMETHING_WENT_WRONG = "Something Went Wrong";
    String _ERR_INTERNAL_SERVER_ERROR = "Internal Server Error";
    String _ERR_UNABLE_TO_CONNECT_SERVER = "Unable To Connect Server";
    String uri = "http://www.etrukr.com/webservice/public/driver_order_documents/";
    String chatUri = "http://www.etrukr.com/webservice/public/chatimages/";
    String IP = BuildConfig.IP;
    String UserRegistration = IP + "ws-register-shipper";
    String UserLogin = IP + "ws-login";
    String GetSourceDestination = IP + "ws-get-destinations";
    String GetTruckType = IP + "ws-truck-type";
    String GetCalculateDistance = IP + "ws-calculate-distance";
    String CreateNewOrder = IP + "ws-new-order";
    String ListCards = IP + "ws-list-cards";
    String AddCard = IP + "ws-add-card";
    String UserLogout = IP + "ws-logout";
    String UserId = IP + "ws-userid";
    String Activationcode = IP + "ws-activationcode";
    String Mobile = IP + "ws-mobile-verify";
    String ResendText = IP + "ws-resend-code";
    String ChangeMobileNumber = IP + "ws-change-mobile";
    String ChangePassword = IP + "ws-update-password";
    String ForgotPassword = IP + "ws-reset-password";
    String UpdateProfileInfo = IP + "ws-update-profile";
    String GetProfile = IP + "ws-get-profile";
    String ProfileUpdate = IP + "ws-update-profile";
    String DeleteAccount = IP + "ws-delete-account";
    String TruckType = IP + "ws-truck-type";
    String EditShipment = IP + "ws-edit-order";
    String ReadShipment = IP + "ws-read-order";
    String DeleteShipment = IP + "ws-delete-order";
    String ListAllShipment = IP + "ws-all-shippment";
    String BorderCrossing = IP + "ws-border-cross";
    String GetDriverDetails = IP + "ws-driver-details";
    String RemoveCard = IP + "ws-remove-card";
    String SetDefaultCard = IP + "ws-set-default";
    String GetDefaultCard = IP + "ws-get-default";
    String ReceiveChat = IP + "ws-receive-chat";
    String SendChat = IP + "ws-send-chat";
    String Notification = IP + "ws-notification";
    String PaymentOptions = IP + "ws-get-payment-options";
    String OrderRating = IP + "ws-order-rating";
    String CancelJob = IP + "ws-cancel-order";
    String ApproveJob = IP + "ws-shipper-approve-job";
    String DriverEnroute = IP + "ws-driver-enroute";
    String EditShipment1 = IP + "ws-shipper-edit-shipment";
    String CreditCardPayment = IP + "ws-credit-card-payment";
    String SavePaymentOptions = IP + "ws-save-payment-options";
    String PayPal = IP + "ws-redirect-paypal";
    String RootDir = "Trucker";
    String SharedImage_Path = RootDir + File.separator + "Images";
    String ImageFormat = ".png";

}
