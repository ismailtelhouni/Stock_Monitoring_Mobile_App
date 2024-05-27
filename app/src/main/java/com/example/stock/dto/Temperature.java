package com.example.stock.dto;

public class Temperature {

    double date , humidity , temperature ;
    boolean valid;

    public Temperature(double date, double humidity, double temperature, boolean valid) {
        this.date = date;
        this.humidity = humidity;
        this.temperature = temperature;
        this.valid = valid;
    }

    public Temperature() {
    }

    public double getDate() {
        return date;
    }

    public void setDate(double date) {
        this.date = date;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "Temperature{" +
                "date=" + date +
                ", humidity=" + humidity +
                ", temperature=" + temperature +
                ", valid=" + valid +
                '}';
    }
}
