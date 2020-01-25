package com.spark.medicinedelivery;

public class BuyersCard {

    String Address, Phone, email, image, username;

    public BuyersCard() {
    }

    public BuyersCard(String address, String phone, String email, String image, String username) {
        Address = address;
        Phone = phone;
        this.email = email;
        this.image = image;
        this.username = username;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
