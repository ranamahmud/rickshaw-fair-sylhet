package com.example.rananoyon.rickshawfairsylhet;

/**
 * Created by Md Rana Mahmud on 12/17/2017.
 */

import android.app.Application;
import com.android.volley.RequestQueue;
public class CustomApplication extends Application{
    private RequestQueue requestQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
    }
    public RequestQueue getVolleyRequestQueue(){
        return requestQueue;
    }
}