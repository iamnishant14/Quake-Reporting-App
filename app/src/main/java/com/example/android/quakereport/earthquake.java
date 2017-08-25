package com.example.android.quakereport;

/**
 * Created by nishant on 4/1/17.
 */

public class earthquake {

    private Double magnitude;
    private String location;
    private String date;

    public earthquake(Double mag,String loc,String dt)
    {
        magnitude=mag;
        location=loc;
        date=dt;
    }

    public Double getMagnitude()
    {
        return magnitude;
    }
    public  String getLocation()
    {
        return location;
    }
    public  String getDate()
    {
        return date;
    }


}
