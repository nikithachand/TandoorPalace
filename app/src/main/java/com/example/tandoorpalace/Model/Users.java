package com.example.tandoorpalace.Model;

public class Users
{
    private String UserName, Password, Phone;

    public Users ()
    {

    }

    public Users(String userName, String password, String phone) {
        UserName = userName;
        Password = password;
        Phone = phone;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
