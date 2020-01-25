package com.spark.medicinedelivery;

public class SubscriptionCard {
    String Med_Name, Quantity, Shop_Name, Total_Price;

    public SubscriptionCard() {
    }

    public SubscriptionCard(String med_Name, String quantity, String shop_Name, String total_Price) {
        Med_Name = med_Name;
        Quantity = quantity;
        Shop_Name = shop_Name;
        Total_Price = total_Price;
    }

    public String getMed_Name() {
        return Med_Name;
    }

    public void setMed_Name(String med_Name) {
        Med_Name = med_Name;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getShop_Name() {
        return Shop_Name;
    }

    public void setShop_Name(String shop_Name) {
        Shop_Name = shop_Name;
    }

    public String getTotal_Price() {
        return Total_Price;
    }

    public void setTotal_Price(String total_Price) {
        Total_Price = total_Price;
    }
}
