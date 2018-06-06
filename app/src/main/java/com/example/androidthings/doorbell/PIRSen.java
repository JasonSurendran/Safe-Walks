package com.example.androidthings.doorbell;

public class PIRSen {
    public String location;
    public String pin;

    public PIRSen(String location, String pin){
        this.location = location;
        this.pin = pin;
    }

    public String getPin(){
        return pin;
    }


    public String getLocation(){
        return location;
    }

}
