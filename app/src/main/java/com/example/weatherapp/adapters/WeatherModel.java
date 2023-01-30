package com.example.weatherapp.adapters;

public class WeatherModel {

    public String date;
    public String currentTemp;
    public String condition;
    public String imageURL;

    public WeatherModel(String date, String currentTemp, String condition, String imageURL) {
        this.date = date;
        this.currentTemp = currentTemp;
        this.condition = condition;
        this.imageURL = imageURL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(String currentTemp) {
        this.currentTemp = currentTemp;
    }


    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
