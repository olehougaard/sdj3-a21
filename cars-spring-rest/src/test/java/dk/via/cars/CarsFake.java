package dk.via.cars;

import dk.via.cars.ws.Cars;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class CarsFake implements Cars {
    private List<Car> cars = new ArrayList<>();

    @Override
    public Car create(String licenseNumber, String model, int year, Money price) {
        if (read(licenseNumber) != null) throw new RuntimeException("duplicate key");
        Car car = new Car(licenseNumber, model, year, price);
        cars.add(car);
        return car;
    }

    @Override
    public List<Car> readAll() {
        return new ArrayList<>(cars);
    }

    @Override
    public Car read(String licenseNumber) {
        Optional<Car> first = cars.stream().filter(c -> c.getLicenseNumber().equals(licenseNumber)).findFirst();
        return first.orElse(null);
    }

    @Override
    public void delete(String licenseNumber) {
        cars.remove(read(licenseNumber));
    }
}
