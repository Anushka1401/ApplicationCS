package com.example.applicationcs;

public class ModelUser {
    private int profileImage;
    private  String userName;
    private String uId;
    private String phonenumber;
    private String email;

    public ModelUser(int profileImage, String userName, String uId, String phonenumber, String email) {
        this.profileImage = profileImage;
        this.userName = userName;
        this.uId = uId;
        this.phonenumber = phonenumber;
        this.email=email;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
