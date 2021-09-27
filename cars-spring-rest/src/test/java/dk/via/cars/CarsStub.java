package dk.via.cars;

import dk.via.cars.ws.Cars;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CarsStub implements Cars {
    private List<Car> cars = new ArrayList<>();

    @Override
    public Car create(String licenseNo, String model, int year, Money price) {
        Car car = new Car();
        car.setLicenseNumber(licenseNo);
        car.setModel(model);
        car.setYear(year);
        car.setPrice(price);
        cars.add(car);
        return car;
    }

    @Override
    public List<Car> readAll() {
        return new ArrayList<>(cars);
    }

    @Override
    public Car read(String licenseNumber) {
        for(Car car: cars)
          if (car.getLicenseNumber().equals(licenseNumber))
            return car;
        return null;
    }

    @Override
    public void delete(String license_number) {
        cars = cars.stream().filter(c -> !c.getLicenseNumber().equals(license_number)).collect(toList());
    }
}
