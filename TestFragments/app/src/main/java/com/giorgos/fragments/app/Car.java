package com.giorgos.fragments.app;

import java.net.URL;

/**
 * Created by giorgos on 12/04/14.
 */
public class Car {
    private Integer id;
    private String brand;
    private String model;
    private Integer year;
    private int imageRes;
    private int brandIcon;

    public Car(Integer id, String brand, int brandIcon, String model, Integer year, int res) {
        this.id = id;
        this.brand = brand;
        this.brandIcon = brandIcon;
        this.model = model;
        this.year = year;
        this.imageRes = res;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public int getBrandIcon() {
        return brandIcon;
    }

    public void setBrandIcon(int brandIcon) {
        this.brandIcon = brandIcon;
    }
}
