package hu.schdesign.solarboat.model;

import hu.schdesign.solarboat.model.Boat.Coordinates;
import hu.schdesign.solarboat.model.Boat.DataAnalysis;
import hu.schdesign.solarboat.model.Boat.Error;

import java.util.ArrayList;
import java.util.List;

public class ResponseBoatData {
    private ArrayList<ArrayList<dataPair<String, Double>>> tilt;
    private ArrayList<ArrayList<dataPair<String, Double>>> acceleration;
    private ArrayList<Coordinates> compass;
    private ArrayList<Double> velocity;
    private ArrayList<ArrayList<dataPair<String, Double>>> battery;
    private ArrayList<ArrayList<dataPair<String, Double>>> motor;
    private ArrayList<Error> errors = new ArrayList<>();
    private String id;
    private ArrayList<DataAnalysis> tiltAnalysis;
    private ArrayList<DataAnalysis> accelerationAnalysis;
    private ArrayList<DataAnalysis> compassAnalysis;
    private ArrayList<DataAnalysis> batteryAnalysis;
    private ArrayList<DataAnalysis> motorAnalysis;

    public ResponseBoatData(){}

    public ArrayList<Error> getErrors() {
        return errors;
    }

    public ArrayList<ArrayList<dataPair<String, Double>>> getTilt() {
        return tilt;
    }

    public void setTilt(ArrayList<ArrayList<dataPair<String, Double>>> tilt) {
        this.tilt = tilt;
    }

    public ArrayList<ArrayList<dataPair<String, Double>>> getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(ArrayList<ArrayList<dataPair<String, Double>>> acceleration) {
        this.acceleration = acceleration;
    }

    public ArrayList<ArrayList<dataPair<String, Double>>> getBattery() {
        return battery;
    }

    public void setBattery(ArrayList<ArrayList<dataPair<String, Double>>> battery) {
        this.battery = battery;
    }

    public ArrayList<ArrayList<dataPair<String, Double>>> getMotor() {
        return motor;
    }

    public void setMotor(ArrayList<ArrayList<dataPair<String, Double>>> motor) {
        this.motor = motor;
    }

    public void setErrors(ArrayList<Error> errors) {
        this.errors = errors;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<DataAnalysis> getTiltAnalysis() {
        return tiltAnalysis;
    }

    public void setTiltAnalysis(ArrayList<DataAnalysis> tiltAnalysis) {
        this.tiltAnalysis = tiltAnalysis;
    }

    public ArrayList<DataAnalysis> getAccelerationAnalysis() {
        return accelerationAnalysis;
    }

    public void setAccelerationAnalysis(ArrayList<DataAnalysis> accelerationAnalysis) {
        this.accelerationAnalysis = accelerationAnalysis;
    }

    public ArrayList<DataAnalysis> getCompassAnalysis() {
        return compassAnalysis;
    }

    public void setCompassAnalysis(ArrayList<DataAnalysis> compassAnalysis) {
        this.compassAnalysis = compassAnalysis;
    }

    public ArrayList<DataAnalysis> getBatteryAnalysis() {
        return batteryAnalysis;
    }

    public void setBatteryAnalysis(ArrayList<DataAnalysis> batteryAnalysis) {
        this.batteryAnalysis = batteryAnalysis;
    }

    public ArrayList<DataAnalysis> getMotorAnalysis() {
        return motorAnalysis;
    }

    public void setMotorAnalysis(ArrayList<DataAnalysis> motorAnalysis) {
        this.motorAnalysis = motorAnalysis;
    }

    public ArrayList<Coordinates> getCompass() {
        return compass;
    }

    public void setCompass(ArrayList<Coordinates> compass) {
        this.compass = compass;
    }

    public ArrayList<Double> getVelocity() {
        return velocity;
    }

    public void setVelocity(ArrayList<Double> velocity) {
        this.velocity = velocity;
    }
}
