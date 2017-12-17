package com.example.rananoyon.rickshawfairsylhet;

/**
 * Created by Md Rana Mahmud on 12/17/2017.
 */
import java.util.List;
public class RouteObject {
    private List<LegsObject> legs;
    public RouteObject(List<LegsObject> legs) {
        this.legs = legs;
    }
    public List<LegsObject> getLegs() {
        return legs;
    }
}