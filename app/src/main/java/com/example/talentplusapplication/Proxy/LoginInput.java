package com.example.talentplusapplication.Proxy;

public class LoginInput {

    private  String userName ="";
    private  String password ="";

    public LoginInput(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    /*@Override
    public String toString() {
        return "LoginInput{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }*/
}
