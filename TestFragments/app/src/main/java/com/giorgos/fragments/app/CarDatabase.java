package com.giorgos.fragments.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giorgos on 12/04/14.
 */
public class CarDatabase {

    private List<Car> cars = new ArrayList<Car>();

    private static CarDatabase singleton;

    public static CarDatabase Instance() {
        if (singleton == null) {
            singleton = new CarDatabase();
        }
        return singleton;
    }

    public CarDatabase() {
        Car crz = new Car(1, "Honda", R.drawable.honda, "CR-Z", 2008, R.drawable.crz);
        Car galant = new Car(2, "Mitsubushi", R.drawable.mitshubishi, "Galant", 1997, R.drawable.galant);
        cars.add(crz);
        cars.add(galant);
    }

    public Car findById(Integer id) {
        for (Car car : cars) {
            if (car.getId().equals(id)) {
                return car;
            }
        }
        return null;
    }

    public List<Car> findAll() {
        return new ArrayList<Car>(cars);
    }
}
