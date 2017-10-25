package com.example.rananoyon.rickshawfairsylhet;

/**
 * Created by Md Rana Mahmud on 10/25/2017.
 */
public class Fair {
    private String number;
    private String location;
    private String fair;

    public Fair(String number, String location, String fair) {
        this.number = number;
        this.location = location;
        this.fair = fair;
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
}
