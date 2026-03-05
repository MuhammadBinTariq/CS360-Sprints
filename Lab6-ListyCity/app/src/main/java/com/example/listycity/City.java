package com.example.listycity;

import java.util.Objects;

/**
 * This is a class that defines a City.
 */
public class City implements Comparable<City> {
    /**
     * The name of the city.
     */
    private String city;

    /**
     * The name of the province.
     */
    private String province;

    /**
     * Constructs a new City object.
     * @param city The name of the city.
     * @param province The name of the province.
     */
    City(String city, String province){
        this.city = city;
        this.province = province;
    }

    /**
     * Gets the name of the city.
     * @return The city name.
     */
    String getCityName(){
        return this.city;
    }

    /**
     * Gets the name of the province.
     * @return The province name.
     */
    String getProvinceName(){
        return this.province;
    }

    /**
     * Compares this city to another city lexicographically based on the city name.
     * @param o The other City to compare to.
     * @return A negative integer, zero, or a positive integer as this city name is less than, equal to, or greater than the specified city name.
     */
    @Override // [cite: 64]
    public int compareTo(City o) {
        return this.city.compareTo(o.getCityName());
    }

    // Overriding equals and hashCode to accurately compare City objects by their values
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city1 = (City) o;
        return city.equals(city1.city) && province.equals(city1.province);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, province);
    }
}