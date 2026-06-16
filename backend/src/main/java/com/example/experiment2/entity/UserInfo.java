package com.example.experiment2.entity;

public class UserInfo {

    private Integer id;
    private String username;
    private String name;
    private String gender;
    private Integer age;
    private String email;
    private String address;
    private String birthday;
    private String registerTime;

    public UserInfo() {
    }

    public UserInfo(Integer id, String username, String name, String gender, Integer age, String email, String address, String birthday, String registerTime) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.address = address;
        this.birthday = birthday;
        this.registerTime = registerTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }
}
