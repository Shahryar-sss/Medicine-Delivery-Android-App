package com.spark.medicinedelivery;

public class EmployeeCard {
    String Phone;
    String email;
    String image;
    String username;
    String Employee_ID;

    public EmployeeCard() {
    }

    public EmployeeCard(String phone, String email, String image, String username, String employee_ID) {
        Phone = phone;
        this.email = email;
        this.image = image;
        this.username = username;
        Employee_ID = employee_ID;
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

    public String getEmployee_ID() {
        return Employee_ID;
    }

    public void setEmployee_ID(String employee_ID) {
        Employee_ID = employee_ID;
    }
}
