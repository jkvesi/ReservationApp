package com.example.reservation.classes;

public class UserDataHolder {
        private static UserDataHolder instance;
        private UserDataClass userData;
        private  SelectedServiceClass selectedService;
        private String pickedDate;
        private String pickedSlot;
        private String selectedCompanyUserName;

    public String getSelectedCompanyUserName() {
        return selectedCompanyUserName;
    }

    public void setSelectedCompanyUserName(final String selectedCompanyUserName) {
        this.selectedCompanyUserName = selectedCompanyUserName;
    }

    public String getPickedSlot() {
        return pickedSlot;
    }

    public void setPickedSlot(final String pickedSlot) {
        this.pickedSlot = pickedSlot;
    }

    public SelectedServiceClass getSelectedService() {
        return selectedService;
    }

    public void setSelectedService(final SelectedServiceClass selectedService) {
        this.selectedService = selectedService;
    }

    public String getPickedDate() {
        return pickedDate;
    }

    public void setPickedDate(final String pickedDate) {
        this.pickedDate = pickedDate;
    }

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
