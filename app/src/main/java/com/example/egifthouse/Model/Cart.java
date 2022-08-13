package com.example.egifthouse.Model;

public class Cart
{
    public String date, discount, pid, pname, price, quantity,time, uid, image;

    public Cart() {
    }

    public Cart(String date, String discount, String pid, String pname, String price, String quantity, String time, String uid, String image) {
        this.date = date;
        this.discount = discount;
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.time = time;
        this.uid = uid;
        this.image = image;
    }

    public String getDatee() {
        return date;
    }

    public void setDatee(String date) {
        this.date = date;
    }

    public String getDiscountt() {
        return discount;
    }

    public void setDiscountt(String discount) {
        this.discount = discount;
    }

    public String getPidd() {
        return pid;
    }

    public void setPidd(String pid) {
        this.pid = pid;
    }

    public String getPnamee() {
        return pname;
    }

    public void setPnamee(String pname) {
        this.pname = pname;
    }

    public String getPricee() {
        return price;
    }

    public void setPricee(String price) {
        this.price = price;
    }

    public String getQuantityy() {
        return quantity;
    }

    public void setQuantityy(String quantity) {
        this.quantity = quantity;
    }

    public String getTimee() {
        return time;
    }

    public void setTimee(String time) {
        this.time = time;
    }

    public String getUidd() {
        return uid;
    }

    public void setUidd(String uid) {
        this.uid = uid;
    }

    public String getImagee() {
        return image;
    }

    public void setImagee(String image) {
        this.image = image;
    }
}
