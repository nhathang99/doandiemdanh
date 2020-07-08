package com.example.doandiemdanh;

import java.sql.Timestamp;

public class courses {
    private  String mamonhoc;
    private  String tenmonhoc;
    private int startime;



    public  courses(){

    }
    public courses(String mamonhoc, String tenmonhoc, int timestart){
        this.mamonhoc=mamonhoc;
        this.tenmonhoc=tenmonhoc;
        this.startime = timestart;
    }
    public String getmamonhoc()
    {
        return mamonhoc;
    }
    public String gettenmonhoc()
    {
        return tenmonhoc;
    }

    public int getstartime(){return startime;}


}
