package com.example.listycitylab3;

import java.io.Serializable;

public class City implements Comparable<City>, Serializable {
    private String name;
    private String province;

    public City(String name, String province) {
        this.name = name;
        this.province = province;
    }

    public String getName() {
        return name;
    }

    public String getProvince() {
        return province;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public int compareTo(City city) {
        return this.name.compareTo(city.getName());
    }
}
