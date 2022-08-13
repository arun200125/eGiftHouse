package com.example.egifthouse.Model;

public class Product
{
    public String Pname,Price, Description,image, Category, pid, Date, Time, uid;

    public Product() {
    }

    public Product(String pname, String price, String description, String image, String category, String pid, String date, String time, String uid) {
        Pname = pname;
        Price = price;
        Description = description;
        this.image = image;
        Category = category;
        this.pid = pid;
        Date = date;
        Time = time;
        this.uid = uid;
    }

    public String getPnamee() {
        return Pname;
    }

    public void setPnamee(String pname) {
        Pname = pname;
    }

    public String getPricee() {
        return Price;
    }

    public void setPricee(String price) {
        Price = price;
    }

    public String getDescriptionn() {
        return Description;
    }

    public void setDescriptionn(String description) {
        Description = description;
    }

    public String getImagee() {
        return image;
    }

    public void setImagee(String image) {
        this.image = image;
    }

    public String getCategoryy() {
        return Category;
    }

    public void setCategoryy(String category) {
        Category = category;
    }

    public String getPidd() {
        return pid;
    }

    public void setPidd(String pid) {
        this.pid = pid;
    }

    public String getDatee() {
        return Date;
    }

    public void setDatee(String date) {
        Date = date;
    }

    public String getTimee() {
        return Time;
    }

    public void setTimee(String time) {
        Time = time;
    }

    public String getUidd() {
        return uid;
    }

    public void setUidd(String uid) {
        this.uid = uid;
    }
}
