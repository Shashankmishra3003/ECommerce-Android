package com.shashank.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

public class Request {
    String fname,lname,mobile,address,city,state,zipcode,country,orderTotal,orderDate,userEmail,orderSubtotal,orderTotalShipping;
    List<Product> orderList = new ArrayList<>();

    public Request() {
    }

    public Request(String fname, String lname, String mobile, String address, String city, String state,
                   String zipcode, String country, String orderTotal, String orderDate, String userEmail,
                   String orderSubtotal, String orderTotalShipping, List<Product> orderList) {
        this.fname = fname;
        this.lname = lname;
        this.mobile = mobile;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.country = country;
        this.orderTotal = orderTotal;
        this.orderDate = orderDate;
        this.userEmail = userEmail;
        this.orderSubtotal = orderSubtotal;
        this.orderTotalShipping = orderTotalShipping;
        this.orderList = orderList;
    }

    public String getOrderSubtotal() {
        return orderSubtotal;
    }

    public void setOrderSubtotal(String orderSubtotal) {
        this.orderSubtotal = orderSubtotal;
    }

    public String getOrderTotalShipping() {
        return orderTotalShipping;
    }

    public void setOrderTotalShipping(String orderTotalShipping) {
        this.orderTotalShipping = orderTotalShipping;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Product> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Product> orderList) {
        this.orderList = orderList;
    }
}
