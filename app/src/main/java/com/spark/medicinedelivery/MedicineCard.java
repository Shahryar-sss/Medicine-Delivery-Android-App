package com.spark.medicinedelivery;

public class MedicineCard {

    private String Name, Price;
    private String Type;

    public MedicineCard() {
    }

    public MedicineCard(String medname, String medprice, String medimage) {
        this.Name = medname;
        this.Price = medprice;
        this.Type = medimage;
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
