package com.example.experiment2.entity;

public class UserInfo {

    private Integer id;
    private String date;
    private String name;
    private String province;
    private String city;
    private String address;
    private String zip;

    public UserInfo() {
    }

    public UserInfo(String date, String name, String province, String city, String address, String zip) {
        this.date = date;
        this.name = name;
        this.province = province;
        this.city = city;
        this.address = address;
        this.zip = zip;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
