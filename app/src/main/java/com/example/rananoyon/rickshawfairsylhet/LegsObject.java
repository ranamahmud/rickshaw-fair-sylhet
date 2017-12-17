package com.example.rananoyon.rickshawfairsylhet;

/**
 * Created by Md Rana Mahmud on 12/17/2017.
 */

import java.util.List;
public class LegsObject {
    private List<StepsObject> steps;
    public LegsObject(List<StepsObject> steps) {
        this.steps = steps;
    }
    public List<StepsObject> getSteps() {
        return steps;
    }
}