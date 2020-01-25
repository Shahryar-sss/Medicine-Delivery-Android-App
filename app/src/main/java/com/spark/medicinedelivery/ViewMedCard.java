package com.spark.medicinedelivery;

public class ViewMedCard {
    String Name, Price, Type;

    public ViewMedCard() {
    }

    public ViewMedCard(String name, String price, String type) {
        Name = name;
        Price = price;
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
