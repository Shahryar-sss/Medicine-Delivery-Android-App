package com.spark.medicinedelivery;

public class SellerCard {

    String Shop_Address, Shop_Phone, Shop_Name, email, image;

    public SellerCard() {
    }

    public SellerCard(String shop_Address, String shop_Phone, String shop_Name, String email, String image) {
        Shop_Address = shop_Address;
        Shop_Phone = shop_Phone;
        Shop_Name = shop_Name;
        this.email = email;
        this.image = image;
    }

    public String getShop_Address() {
        return Shop_Address;
    }

    public void setShop_Address(String shop_Address) {
        Shop_Address = shop_Address;
    }

    public String getShop_Phone() {
        return Shop_Phone;
    }

    public void setShop_Phone(String shop_Phone) {
        Shop_Phone = shop_Phone;
    }

    public String getShop_Name() {
        return Shop_Name;
    }

    public void setShop_Name(String shop_Name) {
        Shop_Name = shop_Name;
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
}
