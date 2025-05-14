package com.example.lab4_20211688.models;

public class HourlyForecastModel {
    private String time;
    private double tempC;
    private String condition;
    private int humidity;
    private double chanceOfRain;

    public HourlyForecastModel(String time, double tempC, String condition, int humidity, double chanceOfRain) {
        this.time = time;
        this.tempC = tempC;
        this.condition = condition;
        this.humidity = humidity;
        this.chanceOfRain = chanceOfRain;
    }

    public String getTime() { return time; }
    public double getTempC() { return tempC; }
    public String getCondition() { return condition; }
    public int getHumidity() { return humidity; }
    public double getChanceOfRain() { return chanceOfRain; }
}
