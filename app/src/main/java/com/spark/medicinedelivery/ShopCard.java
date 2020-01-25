package com.spark.medicinedelivery;

public class ShopCard {
    String Expiry, Name, image;

    public ShopCard() {
    }

    public ShopCard(String expiry, String shop_Name, String image) {
        Expiry = expiry;
        Name = shop_Name;
        this.image = image;
    }

    public String getExpiry() {
        return Expiry;
    }

    public void setExpiry(String expiry) {
        Expiry = expiry;
    }

    public String getName() {
        return Name;
    }

    public void setShop_Name(String name) {
        Name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
