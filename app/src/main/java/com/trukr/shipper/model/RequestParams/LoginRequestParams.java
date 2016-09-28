package com.trukr.shipper.model.RequestParams;


public class LoginRequestParams {

    private String UserType;
    private LoginDetails LoginDetails;

    public LoginRequestParams() {
        UserType = "";
        LoginDetails = new LoginDetails();
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String UserType) {
        this.UserType = UserType;
    }

    public LoginDetails getLoginDetails() {
        return LoginDetails;
    }

    public void setLoginDetails(LoginDetails LoginDetails) {
        this.LoginDetails = LoginDetails;
    }

    public class LoginDetails {

        private String Username;
        private String Password;
        private String DeviceUDID;
        private String DeviceType;
        private String RegId;

        public LoginDetails() {
            Username = "";
            Password = "";
            DeviceUDID = "";
            DeviceType = "";
            RegId = "";
        }

        public String getUsername() {
            return Username;
        }

        public void setUsername(String Username) {
            this.Username = Username;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String Password) {
            this.Password = Password;
        }

        public String getDeviceUDID() {
            return DeviceUDID;
        }

        public void setDeviceUDID(String DeviceUDID) {
            this.DeviceUDID = DeviceUDID;
        }

        public String getDeviceType() {
            return DeviceType;
        }

        public void setDeviceType(String DeviceType) {
            this.DeviceType = DeviceType;
        }

        public String getRegId() {
            return RegId;
        }


        public void setRegId(String RegId) {
            this.RegId = RegId;
        }

    }
}