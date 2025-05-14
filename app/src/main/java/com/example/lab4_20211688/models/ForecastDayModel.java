package com.example.lab4_20211688.models;

public class ForecastDayModel {
    private String date;
    private double maxtemp_c;
    private double mintemp_c;
    private String conditionText;
    private double maxwind_mph;

    public ForecastDayModel(String date, double maxtemp_c, double mintemp_c, String conditionText, double maxwind_mph) {
        this.date = date;
        this.maxtemp_c = maxtemp_c;
        this.mintemp_c = mintemp_c;
        this.conditionText = conditionText;
        this.maxwind_mph = maxwind_mph;
    }

    public String getDate() { return date; }
    public double getMaxtemp_c() { return maxtemp_c; }
    public double getMintemp_c() { return mintemp_c; }
    public String getConditionText() { return conditionText; }
    public double getMaxwind_mph() { return maxwind_mph; }
}
