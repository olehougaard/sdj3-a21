package dk.via.cars;

import java.math.BigDecimal;

public class ObjectFactory {
    public static Money createMoneyDTO(BigDecimal amount, String currency) {
        Money money = new Money();
        money.setAmount(amount);
        money.setCurrency(currency);
        return money;
    }

    public static Car createCarDTO(String licenseNo, String model, int year, Money price) {
        Car car = new Car();
        car.setLicenseNumber(licenseNo);
        car.setModel(model);
        car.setYear(year);
        car.setPrice(price);
        return car;
    }
}
