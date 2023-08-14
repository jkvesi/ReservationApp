package com.example.reservation.classes;

public class UserDataClass {
    String firstName, lastName, phone, country, city, email;
    boolean switcher;

    public UserDataClass(){
    }
    public UserDataClass(String firstName, String lastName, boolean switcher, String phone, String country, String city, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.switcher = switcher;
        this.phone = phone;
        this.country = country;
        this.city = city;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public boolean isSwitcher() {
        return switcher;
    }

    public void setSwitcher(final boolean switcher) {
        this.switcher = switcher;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
