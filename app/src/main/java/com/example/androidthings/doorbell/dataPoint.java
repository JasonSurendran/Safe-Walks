package com.example.androidthings.doorbell;




import java.text.SimpleDateFormat;
import java.util.Calendar;


public class dataPoint {
    public String location;
    public String day;
    public Calendar mCurrentDate;
    public String month;
    public String year;
    public String time;
    public String date;


    public dataPoint(){

    }

    public dataPoint(PIRSen sen){
        mCurrentDate = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd 'at' HH:mm:ss z");
        date = mdformat.format(mCurrentDate.getTime());
        this.location = sen.getLocation();

    }

    public String getLocation(){
        return location;
    }

    public String getDate() {
        return this.date;
    }


}


