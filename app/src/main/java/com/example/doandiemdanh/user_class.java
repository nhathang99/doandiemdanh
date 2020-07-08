package com.example.doandiemdanh;

public class user_class {
    private String userid;
    private String mamon;

    public user_class() {

    }

    public user_class(String userid, String mamon) {
        this.userid = userid;
        this.mamon=mamon;
    }
    public String getUserid(){

        return userid;
    }
    public String getMamon(){

        return mamon;
    }
}