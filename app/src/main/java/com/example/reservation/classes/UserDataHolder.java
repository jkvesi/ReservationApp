package com.example.reservation.classes;

public class UserDataHolder {

        private static UserDataHolder instance;
        private UserDataClass userData;

        private UserDataHolder() {
            userData = new UserDataClass();
        }

        public static UserDataHolder getInstance() {
            if (instance == null) {
                instance = new UserDataHolder();
            }
            return instance;
        }

        public UserDataClass getUserData() {
            return userData;
        }

    public void setUserData(final UserDataClass userData) {
        this.userData = userData;
    }
}
