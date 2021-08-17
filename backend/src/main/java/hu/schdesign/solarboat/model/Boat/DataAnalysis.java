package hu.schdesign.solarboat.model.Boat;

import java.util.ArrayList;

public class DataAnalysis {
    private Double average;
    private Double max;
    private Double min;
    private ArrayList<Double> localMaxPoints;
    private ArrayList<Double> localMinPoints;
    private String name;

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public ArrayList<Double> getLocalMaxPoints() {
        return localMaxPoints;
    }

    public void setLocalMaxPoints(ArrayList<Double> localMaxPoints) {
        this.localMaxPoints = localMaxPoints;
    }

    public ArrayList<Double> getLocalMinPoints() {
        return localMinPoints;
    }

    public void setLocalMinPoints(ArrayList<Double> localMinPoints) {
        this.localMinPoints = localMinPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
