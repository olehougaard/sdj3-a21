package dk.via.cars.ws;


import dk.via.cars.Car;
import dk.via.cars.Money;

import java.util.List;

public interface Cars {
	Car create(String licenseNo, String model, int year, Money price);
	List<Car> readAll();

    Car read(String licenseNumber);

    void delete(String license_number);
}
