package com.example.rananoyon.rickshawfairsylhet;

/**
 * Created by Md Rana Mahmud on 10/25/2017.
 */
public class Fair {
    private String number;
    private String location;
    private String fair;
    private String source;
    private String destionation;

    public Fair(String number, String location, String fair,String source, String destionation) {
        this.number = number;
        this.location = location;
        this.fair = fair;
        this.source = source;
        this.destionation = destionation;
    }

    public String getNumber() {
        return number;
    }

    public String getLocation() {
        return location;
    }

    public String getFair() {
        return fair;
    }
    public  String getSource(){return source;}
    public  String getDestionation(){return  destionation;}
}
