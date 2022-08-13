package com.example.egifthouse.Model;

public class AdminOrders
{
    public String address, city, date, name, phone, state, time, totalAmount, uid;

    public AdminOrders() {
    }

    public AdminOrders(String address, String city, String date, String name, String phone, String state, String time, String totalAmount, String uid) {
        this.address = address;
        this.city = city;
        this.date = date;
        this.name = name;
        this.phone = phone;
        this.state = state;
        this.time = time;
        this.totalAmount = totalAmount;
        this.uid = uid;
    }

    public String getAddresss() {
        return address;
    }

    public void setAddresss(String address) {
        this.address = address;
    }

    public String getCityy() {
        return city;
    }

    public void setCityy(String city) {
        this.city = city;
    }

    public String getDatee() {
        return date;
    }

    public void setDatee(String date) {
        this.date = date;
    }

    public String getNamee() {
        return name;
    }

    public void setNamee(String name) {
        this.name = name;
    }

    public String getPhonee() {
        return phone;
    }

    public void setPhonee(String phone) {
        this.phone = phone;
    }

    public String getStatee() {
        return state;
    }

    public void setStatee(String state) {
        this.state = state;
    }

    public String getTimee() {
        return time;
    }

    public void setTimee(String time) {
        this.time = time;
    }

    public String getTotalAmountt() {
        return totalAmount;
    }

    public void setTotalAmountt(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUidd() {
        return uid;
    }

    public void setUidd(String uid) {
        this.uid = uid;
    }
}
