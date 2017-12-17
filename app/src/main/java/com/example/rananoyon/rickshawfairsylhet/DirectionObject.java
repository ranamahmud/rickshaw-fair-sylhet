package com.example.rananoyon.rickshawfairsylhet;

/**
 * Created by Md Rana Mahmud on 12/17/2017.
 */

import java.util.List;
public class DirectionObject {
    private List<RouteObject> routes;
    private String status;
    public DirectionObject(List<RouteObject> routes, String status) {
        this.routes = routes;
        this.status = status;
    }
    public List<RouteObject> getRoutes() {
        return routes;
    }
    public String getStatus() {
        return status;
    }
}