package com.smartcitypune.smartpune;

/**
 * Created by Kapil on 03-09-2018.
 */

public class Point {
    Double latitude;
    Double longitude;
    Integer day_of_week;
    Integer count;
    Integer time;

    public Point() {
    }

    public Point(Double latitude, Double longitude, Integer day_of_week, Integer count, Integer time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.day_of_week = day_of_week;
        this.count = count;
        this.time = time;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(Integer day_of_week) {
        this.day_of_week = day_of_week;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Point{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", day_of_week=" + day_of_week +
                ", count=" + count +
                ", time=" + time +
                '}';
    }
}
